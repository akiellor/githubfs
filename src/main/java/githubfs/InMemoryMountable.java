package githubfs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMountable implements Mountable {
    private Map<Path, Node> nodes = new ConcurrentHashMap<Path, Node>();

    public InMemoryMountable(){
        nodes.put(Path.ROOT, new Directory());
    }

    @Override public void put(Path path, Node issue) {
        for(Path ancestor : path.ancestors()){
            nodes.put(ancestor, new Directory());
        }
        for(Path registered : nodes.keySet()){
            if(path.isAncestorOf(registered)){
                nodes.remove(registered);
            }
        }
        nodes.put(path, issue);
    }

    @Override public <T> T with(Path path, Handler<T> handler) {
        if(nodes.containsKey(path)){
            handler.found(path, nodes.get(path));
        }
        return handler.result();
    }

    @Override public <T> T all(Handler<T> handler) {
        for(Map.Entry<Path, Node> entry : nodes.entrySet()){
            handler.found(entry.getKey(), entry.getValue());
        }
        return handler.result();
    }
}
