package githubfs;

public interface Node {
    void describe(Output output);

    void update(Input input);

    public interface Output {
        void content(Content content);

        void file();

        void directory();

        void executable();

        void updatedAt(long time);
    }

    public interface Input {
        void content(Content content);
    }
}
