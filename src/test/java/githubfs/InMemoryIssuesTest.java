package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryIssuesTest {
    @Mock Issues.Handler handler;

    Issues issues = new InMemoryIssues();
    Issue foo = new Issue("foo");
    Issue bar = new Issue("bar");

    @Test
    public void shouldPutAndGetByPath() {
        issues.put(new Path("foo"), foo);

        issues.with(new Path("foo"), handler);

        verify(handler).found(new Path("foo"), foo);
    }

    @Test
    public void shouldGetAllElements() {
        issues.put(new Path("foo"), foo);
        issues.put(new Path("bar"), bar);

        issues.all(handler);

        verify(handler).found(new Path("foo"), foo);
        verify(handler).found(new Path("bar"), bar);
    }

    @Test
    public void shouldNotFindElement() {
        issues.with(new Path("foo"), handler);

        verify(handler, never()).found(any(Path.class), any(Issue.class));
    }
}
