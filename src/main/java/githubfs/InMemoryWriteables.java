package githubfs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryWriteables implements Mountable {
    private Map<Path, Node> writables = new ConcurrentHashMap<Path, Node>();

    public InMemoryWriteables(){
        writables.put(new Path("/"), new Directory());
    }

    @Override public void put(Path path, Node issue) {
        writables.put(path, issue);
    }

    @Override public <T> T with(Path path, Handler<T> handler) {
        if(writables.containsKey(path)){
            handler.found(path, writables.get(path));
        }
        return handler.result();
    }

    @Override public <T> T all(Handler<T> handler) {
        for(Map.Entry<Path, Node> entry : writables.entrySet()){
            handler.found(entry.getKey(), entry.getValue());
        }
        return handler.result();
    }
}
