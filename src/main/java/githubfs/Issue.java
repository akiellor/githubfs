package githubfs;

public class Issue implements Node {
    private final long createdAt;
    private final Content body;

    private long updatedAt;

    public Issue(Long modified, Content body) {
        this(modified, 0L, body);
    }

    public Issue(long createdAt, long updatedAt, Content body) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.body = body;
    }

    public void describe(Output output) {
        output.content(body);
        output.updatedAt(updatedAt);
        output.createdAt(createdAt);
        output.file();
    }

    @Override public void update(Input input) {
        input.content(body);
        updatedAt = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        if (createdAt != issue.createdAt) return false;
        if (updatedAt != issue.updatedAt) return false;
        if (body != null ? !body.equals(issue.body) : issue.body != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (createdAt ^ (createdAt >>> 32));
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (int) (updatedAt ^ (updatedAt >>> 32));
        return result;
    }
}
