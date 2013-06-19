package githubfs;

import java.util.Map;

public interface Mountable {
    void put(Path path, Node writable);

    <T> T with(Path path, Handler<T> handler);

    <T> T list(Path path, ListHandler<T> listHandler);

    public interface Handler<T> {
        T found(Path path, Node node);
        T notFound(Path path);
    }

    public interface ListHandler<T> {
        T found(Map<Path, Node> entries);
        T notFound(Path path);
    }
}
