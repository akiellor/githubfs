package githubfs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class PathTest {
    @Rule public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldBeRoot() {
        assertEquals(new Path("/"), Path.ROOT);
    }

    @Test
    public void shouldHaveBasename() {
        assertThat(new Path("/foo").basename(), equalTo("foo"));
        assertThat(new Path("/foo/bar").basename(), equalTo("bar"));
    }

    @Test
    public void shouldParentOnRootNodeShouldThrowException() {
        exception.expect(IllegalStateException.class);

        Path.ROOT.parent();
    }

    @Test
    public void shouldHaveParent() {
        assertThat(new Path("/foo").parent(), equalTo(Path.ROOT));

        assertTrue(new Path("/foo").isParentOf(new Path("/foo/bar")));
        assertTrue(Path.ROOT.isParentOf(new Path("/foo")));
        assertFalse(new Path("/foo").isParentOf(Path.ROOT));
        assertFalse(new Path("/foo/bar").isParentOf(Path.ROOT));
        assertFalse(new Path("/foo/bar/baz").isParentOf(new Path("/foo")));
    }

    @Test
    public void shouldProvideAncestorPaths() {
        assertTrue(Path.ROOT.ancestors().isEmpty());
        assertThat(new Path("/foo").ancestors(), hasItems(Path.ROOT));
        assertThat(new Path("/foo/bar").ancestors(), hasItems(Path.ROOT, new Path("/foo")));

        assertTrue(Path.ROOT.isAncestorOf(new Path("/foo")));
        assertTrue(Path.ROOT.isAncestorOf(new Path("/foo/bar")));
        assertTrue(new Path("/foo").isAncestorOf(new Path("/foo/bar")));
        assertFalse(new Path("/foo").isAncestorOf(Path.ROOT));
        assertFalse(new Path("/foo/bar").isAncestorOf(new Path("/foo")));
    }
}
