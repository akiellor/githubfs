package githubfs;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GitHubIssuesMountable implements Mountable{
    private final GHRepository repository;
    private final Map<Path, Node> nodes;

    public GitHubIssuesMountable(GHRepository repository) {
        this.repository = repository;
        this.nodes = new HashMap<Path, Node>();
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
        synchronized (nodes){
            try {
                nodes.clear();
                nodes.put(Path.ROOT, new Directory());
                for (GHIssue issue : repository.getIssues(GHIssueState.OPEN)) {
                    nodes.put(new Path("/" + issue.getNumber()), toIssue(issue));
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private Issue toIssue(GHIssue issue) {
        return new Issue(issue.getCreatedAt().getTime(), issue.getUpdatedAt().getTime(), Content.from(issue.getBody()));
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
                node.describe(new Node.Output() {
                    @Override public void content(Content content) {
                        try {
                            repository.getIssue(Integer.valueOf(usage.path().basename())).setBody(content.getContent());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override public void updatedAt(Long time) {
                    }

                    @Override public void createdAt(Long time) {
                    }

                    @Override public void file() {
                    }

                    @Override public void directory() {
                    }

                    @Override public void executable() {
                    }
                });
            }
        }
    }
}
