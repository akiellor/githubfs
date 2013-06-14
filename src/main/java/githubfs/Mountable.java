package githubfs;

public interface Mountable {
    void put(Path path, Node writable);

    <T> T with(Path path, Handler<T> handler);

    <T> T all(Handler<T> handler);

    public interface Handler<T> {
        void found(Path path, Node node);
        T result();
    }
}
