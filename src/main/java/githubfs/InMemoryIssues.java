package githubfs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryIssues implements Issues{
    private Map<Path, Issue> issues = new ConcurrentHashMap<Path, Issue>();

    @Override public void put(Path path, Issue issue) {
        issues.put(path, issue);
    }

    @Override public void with(Path path, Handler handler) {
        handler.found(issues.get(path));
    }

    @Override public void all(Handler handler) {
        for(Issue issue : issues.values()){
            handler.found(issue);
        }
    }
}
