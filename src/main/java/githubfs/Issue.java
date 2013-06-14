package githubfs;

public class Issue implements Node {
    private final String body;

    public Issue(String body) {
        this.body = body;
    }

    public void describe(File file) {
        file.content(body);
        file.file();
    }
}
