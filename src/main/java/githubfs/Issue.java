package githubfs;

public class Issue {
    private final String body;

    public Issue(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void write(File file) {
        file.content(body);
        file.readable();
    }
}
