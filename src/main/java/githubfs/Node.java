package githubfs;

public interface Node {
    void describe(Output output);

    public interface Output {
        void content(String content);

        void file();

        void directory();

        void executable();
    }
}
