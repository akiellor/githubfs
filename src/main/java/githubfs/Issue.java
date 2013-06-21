package githubfs;

public class Issue implements Node {
    private Long modified;
    private final Content body;

    public Issue(Long modified, Content body) {
        this.modified = modified;
        this.body = body;
    }

    public void describe(Output output) {
        output.content(body);
        output.updatedAt(modified);
        output.file();
    }

    @Override public void update(Input input) {
        input.content(body);
        modified = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        if (body != null ? !body.equals(issue.body) : issue.body != null) return false;
        if (modified != null ? !modified.equals(issue.modified) : issue.modified != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = modified != null ? modified.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
