package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryWriteablesTest {
    @Mock Mountable.Handler handler;

    Mountable mountable = new InMemoryWriteables();
    Issue foo = new Issue("foo");
    Issue bar = new Issue("bar");

    @Test
    public void shouldPutAndGetByPath() {
        mountable.put(new Path("foo"), foo);

        mountable.with(new Path("foo"), handler);

        verify(handler).found(new Path("foo"), foo);
    }

    @Test
    public void shouldGetAllElements() {
        mountable.put(new Path("foo"), foo);
        mountable.put(new Path("bar"), bar);

        mountable.all(handler);

        verify(handler).found(new Path("foo"), foo);
        verify(handler).found(new Path("bar"), bar);
    }

    @Test
    public void shouldNotFindElement() {
        mountable.with(new Path("foo"), handler);

        verify(handler, never()).found(any(Path.class), any(Issue.class));
    }
}
