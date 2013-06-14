package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryMountableTest {
    @Mock Mountable.Handler<Integer> handler;

    Mountable mountable = new InMemoryMountable();
    Issue foo = new Issue("/foo");
    Issue bar = new Issue("/bar");

    @Test
    public void shouldHaveRootByDefault() {
        mountable.with(Path.ROOT, handler);

        verify(handler).found(eq(Path.ROOT), any(Directory.class));
    }

    @Test
    public void shouldPutAndGetByPath() {
        mountable.put(new Path("/foo"), foo);

        mountable.with(new Path("/foo"), handler);

        verify(handler).found(new Path("/foo"), foo);
    }

    @Test
    public void shouldGetAllElements() {
        mountable.put(new Path("/foo"), foo);
        mountable.put(new Path("/bar"), bar);

        mountable.all(handler);

        verify(handler).found(new Path("/foo"), foo);
        verify(handler).found(new Path("/bar"), bar);
    }

    @Test
    public void shouldNotFindElement() {
        mountable.with(new Path("/foo"), handler);

        verify(handler, never()).found(any(Path.class), any(Issue.class));
    }

    @Test
    public void shouldReturnHandlerResultForWith() {
        when(handler.result()).thenReturn(0);

        int result = mountable.with(new Path("/foo"), handler);

        assertEquals(0, result);
    }

    @Test
    public void shouldReturnHandlerResultForAll() {
        when(handler.result()).thenReturn(0);

        int result = mountable.all(handler);

        assertEquals(0, result);
    }

    @Test
    public void shouldPutIntermediateDirectories() {
        mountable.put(new Path("/foo/bar/baz"), foo);

        mountable.all(handler);

        verify(handler).found(eq(Path.ROOT), any(Directory.class));
        verify(handler).found(eq(new Path("/foo")), any(Directory.class));
        verify(handler).found(eq(new Path("/foo/bar")), any(Directory.class));
        verify(handler).found(new Path("/foo/bar/baz"), foo);
    }

    @Test
    public void shouldShouldRemoveDescendantsWhenOverwriting() {
        mountable.put(new Path("/foo/bar"), foo);
        mountable.put(new Path("/foo"), foo);

        mountable.all(handler);

        verify(handler).found(eq(Path.ROOT), any(Directory.class));
        verify(handler).found(eq(new Path("/foo")), any(Directory.class));
        verify(handler).result();
        verifyNoMoreInteractions(handler);
    }
}
