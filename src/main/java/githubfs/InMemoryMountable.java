package githubfs;

import com.google.common.collect.Maps;

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
            return handler.found(path, nodes.get(path));
        }else{
            return handler.notFound(path);
        }
    }

    @Override public <T> T list(Path path, ListHandler<T> listHandler) {
        if(!nodes.keySet().contains(path)){
            return listHandler.notFound(path);
        }
        Map<Path, Node> found = Maps.newHashMap();
        for(Map.Entry<Path, Node> entry : nodes.entrySet()){
            if(path.isParentOf(entry.getKey())){
                found.put(entry.getKey(), entry.getValue());
            }
        }
        return listHandler.found(found);
    }
}
