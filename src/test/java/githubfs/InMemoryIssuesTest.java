package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryIssuesTest {
    @Mock Issues.Handler handler;

    Issues issues = new InMemoryIssues();

    @Test
    public void shouldPutAndGetByPath() {
        Issue issue = new Issue();
        issues.put(new Path("foo"), issue);

        issues.with(new Path("foo"), handler);

        verify(handler).found(new Path("foo"), issue);
    }

    @Test
    public void shouldGetAllElements() {
        Issue foo = new Issue();
        Issue bar = new Issue();
        issues.put(new Path("foo"), foo);
        issues.put(new Path("bar"), bar);

        issues.all(handler);

        verify(handler).found(new Path("foo"), foo);
        verify(handler).found(new Path("bar"), bar);
    }
}
