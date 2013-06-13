package githubfs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryIssues implements Issues{
    private Map<Path, Issue> issues = new ConcurrentHashMap<Path, Issue>();

    @Override public void put(Path path, Issue issue) {
        issues.put(path, issue);
    }

    @Override public void with(Path path, Handler handler) {
        if(issues.containsKey(path)){
            handler.found(path, issues.get(path));
        }
    }

    @Override public void all(Handler handler) {
        for(Map.Entry<Path, Issue> entry : issues.entrySet()){
            handler.found(entry.getKey(), entry.getValue());
        }
    }
}
