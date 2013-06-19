package githubfs;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryMountableTest {
    @Mock Mountable.Handler<Integer> handler;
    @Mock Mountable.ListHandler<Integer> listHandler;
    @Mock Issue foo;
    @Mock Issue bar;
    @Mock Issue qux;

    Mountable mountable = new InMemoryMountable();

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
    @SuppressWarnings("unchecked")
    public void shouldFindIntermediatePaths() {
        mountable.put(new Path("/foo/bar"), bar);
        mountable.put(new Path("/foo/baz/qux"), qux);

        mountable.list(Path.ROOT, listHandler);

        verify(listHandler).found(argThat((Matcher<Map<Path, Node>>)allOf(
                (Matcher) hasKey(new Path("/foo")),
                (Matcher) hasValue(instanceOf(Directory.class)))));
    }

    @Test
    public void shouldNotFindAnyElements() {
        mountable.put(new Path("/foo/bar"), bar);

        mountable.list(new Path("/bar"), listHandler);

        verify(listHandler).notFound(new Path("/bar"));
    }

    @Test
    public void shouldReturnNotFoundValueWhenNoElementsFound() {
        when(listHandler.notFound(any(Path.class)))
                .thenReturn(5);
        mountable.put(new Path("/foo/bar"), bar);

        Integer result = mountable.list(new Path("/bar"), listHandler);

        assertThat(result, equalTo(5));
    }

    @Test
    public void shouldReturnHandlerResultForAll() {
        when(listHandler.found(anyMapOf(Path.class, Node.class))).thenReturn(0);

        int result = mountable.list(new Path("/"), listHandler);

        assertEquals(0, result);
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
    public void shouldPutIntermediateDirectories() {
        mountable.put(new Path("/foo/bar/baz"), foo);

        mountable.with(new Path("/foo"), handler);
        mountable.with(new Path("/foo/bar"), handler);
        mountable.with(new Path("/foo/bar/baz"), handler);

        verify(handler).found(eq(new Path("/foo")), any(Directory.class));
        verify(handler).found(eq(new Path("/foo/bar")), any(Directory.class));
        verify(handler).found(new Path("/foo/bar/baz"), foo);
    }

    @Test
    public void shouldShouldRemoveDescendantsWhenOverwriting() {
        mountable.put(new Path("/foo/bar"), foo);
        mountable.put(new Path("/foo"), foo);

        mountable.with(new Path("/foo"), handler);
        mountable.with(new Path("/foo/bar"), handler);

        verify(handler).found(new Path("/foo"), foo);
        verify(handler, never()).found(eq(new Path("/foo/bar")), any(Directory.class));
    }
}
