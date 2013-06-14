package githubfs;

public class Directory implements Node {
    @Override public void describe(Output output) {
        output.directory();
    }
}
