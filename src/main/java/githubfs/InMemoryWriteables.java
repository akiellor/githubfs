package githubfs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryWriteables implements Mountable {
    private Map<Path, Node> writables = new ConcurrentHashMap<Path, Node>();

    @Override public void put(Path path, Node issue) {
        writables.put(path, issue);
    }

    @Override public void with(Path path, Handler handler) {
        if(writables.containsKey(path)){
            handler.found(path, writables.get(path));
        }
    }

    @Override public void all(Handler handler) {
        for(Map.Entry<Path, Node> entry : writables.entrySet()){
            handler.found(entry.getKey(), entry.getValue());
        }
    }
}
