package githubfs;

public class Directory implements Node {
    @Override public void describe(Output output) {
        output.directory();
    }

    @Override public void update(Input input) {
    }
}
