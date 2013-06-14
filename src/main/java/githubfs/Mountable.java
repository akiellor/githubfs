package githubfs;

public interface Mountable {
    void put(Path path, Node writable);

    void with(Path path, Handler handler);

    void all(Handler handler);

    public interface Handler {
        void found(Path path, Node writeable);
    }
}
