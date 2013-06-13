package githubfs;

import net.fusejna.DirectoryFiller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DirectoryFiller.class)
public class ListingIssueHandlerTest {
    @Mock DirectoryFiller filler;
    @Mock Issue issue;

    @Test
    public void shouldAddDirectoryForFoundPath() {
        ListingIssueHandler handler = new ListingIssueHandler(new Path("/"), filler);

        handler.found(new Path("/foo"), issue);

        verify(filler).add("foo");
    }

    @Test
    public void shouldAddDirectoryRelativeToProvidedPath() {
        ListingIssueHandler handler = new ListingIssueHandler(new Path("/foo"), filler);

        handler.found(new Path("/foo/bar"), issue);

        verify(filler).add("bar");
    }

    @Test
    public void shouldAddDirectoryOnlyIfTheRelativeDirectoryIsTheParent() {
        ListingIssueHandler handler = new ListingIssueHandler(new Path("/foo"), filler);

        handler.found(new Path("/foo/bar/baz"), issue);

        verify(filler, never()).add(anyString());
    }
}
