package githubfs;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GitHubIssuesMountable implements Mountable{
    private final GHRepository repository;
    private final Map<Path, Node> nodes;
    private final Set<Path> marked;

    public GitHubIssuesMountable(GHRepository repository) {
        this.repository = repository;
        this.nodes = new HashMap<Path, Node>();
        this.marked = new HashSet<Path>();
    }

    @Override public <T> T with(Usage usage, Handler<T> handler) {
        if(!refresh()) { return handler.notFound(usage.path()); }
        Path path = usage.path();
        if (nodes.containsKey(path)) {
            return handler.found(path, nodes.get(path), new GitHubControl(usage, nodes.get(path)));
        } else {
            return handler.notFound(path);
        }
    }

    @Override public <T> T list(Path path, ListHandler<T> listHandler) {
        if(!refresh()) { return listHandler.notFound(path); }
        return listHandler.found(Maps.filterKeys(nodes, Predicates.not(Path.IS_ROOT)));
    }

    private boolean refresh(){
        try {
            nodes.put(Path.ROOT, new Directory());
            for (GHIssue issue : repository.getIssues(GHIssueState.OPEN)) {
                Path path = new Path("/" + issue.getNumber());
                if(!marked.contains(path)) {
                    nodes.put(path, toIssue(issue));
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Issue toIssue(GHIssue issue) {
        return new Issue(issue.getCreatedAt().getTime(), issue.getUpdatedAt().getTime(), Content.from(issue.getTitle() + "\n" + issue.getBody()));
    }

    private class GitHubControl implements Control{
        private final Usage usage;
        private final Node node;

        public GitHubControl(Usage usage, Node node){
            this.usage = usage;
            this.node = node;
        }

        @Override public void release() {
            if(usage.isWrite()){
                marked.remove(usage.path());
                node.describe(new Node.AbstractOutput() {
                    @Override public void content(Content content) {
                        try {
                            GHIssue issue = repository.getIssue(Integer.valueOf(usage.path().basename()));
                            issue.setTitle(content.getTitle());
                            issue.setBody(content.getBody());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }

        @Override public void open() {
            if(usage.isWrite()){
                refresh();
                marked.add(usage.path());
            }
        }

        @Override public void unlink() {
            try {
                repository.getIssue(Integer.valueOf(usage.path().basename())).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
