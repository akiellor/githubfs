package githubfs;

import java.util.Map;

public interface Mountable {
    void put(Path path, Node writable);

    <T> T with(Path path, Handler<T> handler);

    <T> T list(Path path, ListHandler<T> listHandler);

    public interface Handler<T> {
        void found(Path path, Node node);
        T result();
    }

    public interface ListHandler<T> {
        T found(Map<Path, Node> entries);
        T notFound(Path path);
    }
}
