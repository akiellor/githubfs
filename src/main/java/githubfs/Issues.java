package githubfs;

public interface Issues {
    void put(Path path, Issue issue);

    void with(Path path, Handler handler);

    void all(Handler handler);

    public interface Handler {
        void found(Path path, Issue issue);
    }
}
