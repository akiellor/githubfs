package githubfs;

import com.google.common.collect.ImmutableMap;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;

import java.util.List;

public class GitHubIssuesMountable implements Mountable{
    private final GHRepository repository;

    public GitHubIssuesMountable(GHRepository repository) {
        this.repository = repository;
    }

    @Override public void put(Path path, Node writable) {
        throw new UnsupportedOperationException();
    }

    @Override public <T> T with(Path path, Handler<T> handler) {
        if(Path.ROOT.equals(path)){
            return handler.found(path, new Directory());
        }
        GHIssue ghIssue;
        try {
            int ghIssueId = Integer.parseInt(path.basename());
            ghIssue = repository.getIssue(ghIssueId);
        } catch (Exception e) {
            return handler.notFound(path);
        }

        if(ghIssue == null){
            return handler.notFound(path);
        }else{
            return handler.found(path, toIssue(ghIssue));
        }
    }

    @Override public <T> T list(Path path, ListHandler<T> listHandler) {
        List<GHIssue> issues;
        try {
            issues = repository.getIssues(GHIssueState.OPEN);
        } catch (Exception e) {
            return listHandler.notFound(path);
        }

        ImmutableMap.Builder<Path, Node> builder = ImmutableMap.builder();
        for(GHIssue issue : issues){
            builder.put(new Path("/" + issue.getNumber()), toIssue(issue));
        }

        return listHandler.found(builder.build());
    }

    private Issue toIssue(GHIssue issue) {
        return new Issue(Content.from(issue.getBody()));
    }
}
