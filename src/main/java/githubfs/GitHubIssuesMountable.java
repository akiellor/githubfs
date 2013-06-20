package githubfs;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GitHubIssuesMountable implements Mountable{
    private final GHRepository repository;
    private final Map<Path, Node> nodes;

    public GitHubIssuesMountable(GHRepository repository) {
        this.repository = repository;
        this.nodes = new ConcurrentHashMap<Path, Node>();
    }

    @Override public void put(Path path, Node writable) {
        throw new UnsupportedOperationException();
    }

    @Override public <T> T with(Path path, Handler<T> handler) {
        if (!refresh()) { return handler.notFound(path); }
        if (nodes.containsKey(path)) {
            return handler.found(path, nodes.get(path));
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
        return new Issue(Content.from(issue.getBody()));
    }
}
