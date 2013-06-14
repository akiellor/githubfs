package githubfs;

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
public class ListingIssueHandlerTest {
    @Mock DirectoryFiller filler;
    @Mock Node writeable;

    @Test
    public void shouldHaveENOENTWhenNothingFound() {
        ListingIssueHandler handler = new ListingIssueHandler(Path.ROOT, filler);

        assertThat(handler.result(), equalTo(-ErrorCodes.ENOENT));
    }

    @Test
    public void shouldHave0ResultWhenFoundSomething() {
        ListingIssueHandler handler = new ListingIssueHandler(Path.ROOT, filler);

        handler.found(new Path("/foo"), writeable);

        assertThat(handler.result(), equalTo(0));
    }

    @Test
    public void shouldAddDirectoryForFoundPath() {
        ListingIssueHandler handler = new ListingIssueHandler(Path.ROOT, filler);

        handler.found(new Path("/foo"), writeable);

        verify(filler).add(".");
        verify(filler).add("..");
        verify(filler).add("foo");
    }

    @Test
    public void shouldAddDirectoryRelativeToProvidedPath() {
        ListingIssueHandler handler = new ListingIssueHandler(new Path("/foo"), filler);

        handler.found(new Path("/foo/bar"), writeable);

        verify(filler).add(".");
        verify(filler).add("..");
        verify(filler).add("bar");
    }

    @Test
    public void shouldNotAddDotDirsForMultipleFiles() {
        ListingIssueHandler handler = new ListingIssueHandler(new Path("/foo"), filler);

        handler.found(new Path("/foo/bar"), writeable);
        handler.found(new Path("/foo/baz"), writeable);

        verify(filler).add(".");
        verify(filler).add("..");
        verify(filler).add("bar");
    }

    @Test
    public void shouldAddDirectoryOnlyIfTheRelativeDirectoryIsTheParent() {
        ListingIssueHandler handler = new ListingIssueHandler(new Path("/foo"), filler);

        handler.found(new Path("/foo/bar/baz"), writeable);

        verify(filler, never()).add(anyString());
    }
}
