package githubfs;

import java.util.Map;

public interface Mountable {
    <T> T with(Usage usage, Handler<T> handler);

    <T> T list(Path path, ListHandler<T> listHandler);

    public interface Handler<T> {
        T found(Path path, Node node, Control control);
        T notFound(Path path);
    }

    public interface ListHandler<T> {
        T found(Map<Path, Node> entries);
        T notFound(Path path);
    }

    public interface Control {
        void release();
        void open();
        void unlink();
    }
}
