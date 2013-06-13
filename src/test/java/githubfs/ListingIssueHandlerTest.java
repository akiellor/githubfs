package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ListingIssueHandlerTest {
    @Mock DirectoryListing listing;
    @Mock Issue issue;

    @Test
    public void shouldAddDirectoryForFoundPath() {
        ListingIssueHandler handler = new ListingIssueHandler(new Path("/"), listing);

        handler.found(new Path("/foo"), issue);

        verify(listing).add("foo");
    }

    @Test
    public void shouldAddDirectoryRelativeToProvidedPath() {
        ListingIssueHandler handler = new ListingIssueHandler(new Path("/foo"), listing);

        handler.found(new Path("/foo/bar"), issue);

        verify(listing).add("bar");
    }

    @Test
    public void shouldAddDirectoryOnlyIfTheRelativeDirectoryIsTheParent() {
        ListingIssueHandler handler = new ListingIssueHandler(new Path("/foo"), listing);

        handler.found(new Path("/foo/bar/baz"), issue);

        verify(listing, never()).add(anyString());
    }
}
