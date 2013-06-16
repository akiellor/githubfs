package githubfs;

public class Issue implements Node {
    private final Content body;

    public Issue(Content body) {
        this.body = body;
    }

    public void describe(Output output) {
        output.content(body);
        output.file();
    }
}
