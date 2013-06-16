package githubfs;

import githubfs.handler.ReadDirHandler;
import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DirectoryFiller.class)
public class ReadDirHandlerTest {
    @Mock DirectoryFiller filler;
    @Mock Node node;

    @Test
    public void shouldHaveENOENTWhenNothingFound() {
        ReadDirHandler handler = new ReadDirHandler(Path.ROOT, filler);

        assertThat(handler.result(), equalTo(-ErrorCodes.ENOENT));
    }

    @Test
    public void shouldHave0ResultWhenFoundSomething() {
        ReadDirHandler handler = new ReadDirHandler(Path.ROOT, filler);

        handler.found(new Path("/foo"), node);

        assertThat(handler.result(), equalTo(0));
    }

    @Test
    public void shouldAddDirectoryForFoundPath() {
        ReadDirHandler handler = new ReadDirHandler(Path.ROOT, filler);

        handler.found(new Path("/foo"), node);

        verify(filler).add(".");
        verify(filler).add("..");
        verify(filler).add("foo");
    }

    @Test
    public void shouldAddDirectoryRelativeToProvidedPath() {
        ReadDirHandler handler = new ReadDirHandler(new Path("/foo"), filler);

        handler.found(new Path("/foo/bar"), node);

        verify(filler).add(".");
        verify(filler).add("..");
        verify(filler).add("bar");
    }

    @Test
    public void shouldNotAddDotDirsForMultipleFiles() {
        ReadDirHandler handler = new ReadDirHandler(new Path("/foo"), filler);

        handler.found(new Path("/foo/bar"), node);
        handler.found(new Path("/foo/baz"), node);

        verify(filler).add(".");
        verify(filler).add("..");
        verify(filler).add("bar");
    }

    @Test
    public void shouldAddDirectoryOnlyIfTheRelativeDirectoryIsTheParent() {
        ReadDirHandler handler = new ReadDirHandler(new Path("/foo"), filler);

        handler.found(new Path("/foo/bar/baz"), node);

        verify(filler, never()).add(anyString());
    }
}
