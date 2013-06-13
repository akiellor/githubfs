package githubfs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathTest {
    @Rule public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldHaveBasename() {
        assertThat(new Path("/foo").basename(), equalTo("foo"));
        assertThat(new Path("/foo/bar").basename(), equalTo("bar"));
    }

    @Test
    public void shouldParentOnRootNodeShouldThrowException() {
        exception.expect(IllegalStateException.class);

        new Path("/").parent();
    }

    @Test
    public void shouldHaveParent() {
        assertThat(new Path("/foo").parent(), equalTo(new Path("/")));

        assertTrue(new Path("/foo").isParentOf(new Path("/foo/bar")));
        assertTrue(new Path("/").isParentOf(new Path("/foo")));
        assertFalse(new Path("/foo").isParentOf(new Path("/")));
        assertFalse(new Path("/foo/bar").isParentOf(new Path("/")));
        assertFalse(new Path("/foo/bar/baz").isParentOf(new Path("/foo")));
    }
}
