package githubfs;

public interface Node {
    void describe(Output output);

    void update(Input input);

    public interface Output {
        void content(Content content);

        void file();

        void directory();

        void executable();

        void updatedAt(Long time);

        void createdAt(Long time);
    }

    public static class AbstractOutput implements Output{
        @Override public void content(Content content) {
        }

        @Override public void file() {
        }

        @Override public void directory() {
        }

        @Override public void executable() {
        }

        @Override public void updatedAt(Long time) {
        }

        @Override public void createdAt(Long time) {
        }
    }

    public interface Input {
        void content(Content content);
    }
}
