package githubfs;

public class Directory implements Node {
    @Override public void describe(File file) {
        file.directory();
    }
}
