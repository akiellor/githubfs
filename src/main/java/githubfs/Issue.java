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

    @Override public void update(Input input) {
        input.content(body);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        if (body != null ? !body.equals(issue.body) : issue.body != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return body != null ? body.hashCode() : 0;
    }
}
