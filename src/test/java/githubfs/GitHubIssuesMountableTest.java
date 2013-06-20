package githubfs;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GitHubIssuesMountableTest {
    @Mock GHRepository repository;
    @Mock Mountable.ListHandler<Integer> listHandler;
    @Mock Mountable.Handler<Integer> handler;
    @Mock GHIssue ghIssue;
    @Mock Node node;

    Issue issue;

    @Before
    public void setup() throws IOException {
        when(ghIssue.getNumber()).thenReturn(1);
        when(ghIssue.getBody()).thenReturn("Foo");
        when(repository.getIssues(GHIssueState.OPEN)).thenReturn(ImmutableList.of(ghIssue));
        when(repository.getIssue(1)).thenReturn(ghIssue);
        issue = new Issue(Content.from("Foo"));
    }

    @Test
    public void shouldHaveADirectoryRoot() {
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(Path.ROOT.forRead(), handler);

        verify(handler).found(eq(Path.ROOT), any(Directory.class), any(Mountable.Control.class));
    }

    @Test
    public void shouldListNoIssues() throws IOException {
        when(repository.getIssues(GHIssueState.OPEN))
                .thenReturn(Lists.<GHIssue>newArrayList());
        when(listHandler.found(anyMapOf(Path.class, Node.class)))
                .thenReturn(0);

        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        Integer result = mountable.list(Path.ROOT, listHandler);

        assertThat(result, equalTo(0));
        verify(listHandler).found(ImmutableMap.<Path, Node>of());
    }

    @Test
    public void shouldListIssuesWhenIssuesArePresent() throws IOException {
        when(repository.getIssues(GHIssueState.OPEN))
                .thenReturn(ImmutableList.of(ghIssue));
        when(listHandler.found(anyMapOf(Path.class, Node.class)))
                .thenReturn(0);

        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        Integer result = mountable.list(Path.ROOT, listHandler);

        assertThat(result, equalTo(0));
        verify(listHandler).found(ImmutableMap.<Path, Node>of(new Path("/1"), issue));
    }

    @Test
    public void shouldBeNotFoundWhenGetIssuesThrowsException() throws IOException {
        when(repository.getIssues(GHIssueState.OPEN)).thenThrow(new RuntimeException());
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.list(Path.ROOT, listHandler);

        verify(listHandler).notFound(Path.ROOT);
    }

    @Test
    public void shouldFindIssueById() throws IOException {
        when(repository.getIssues(GHIssueState.OPEN)).thenReturn(ImmutableList.of(ghIssue));
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forRead(), handler);

        verify(handler).found(eq(new Path("/1")), eq(issue), any(Mountable.Control.class));
    }

    @Test
    public void shouldNotFindIssueById() throws IOException {
        when(repository.getIssues(GHIssueState.OPEN)).thenReturn(Lists.<GHIssue>newArrayList());
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forRead(), handler);

        verify(handler).notFound(new Path("/1"));
    }

    @Test
    public void shouldNotFindIssueByIdWhenPathIsJunk() throws IOException {
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/foobar").forRead(), handler);

        verify(handler).notFound(new Path("/foobar"));
    }

    @Test
    public void shouldNotFindIssueByIdWhenRepositoryThrows() throws IOException {
        when(repository.getIssues(GHIssueState.OPEN)).thenThrow(new RuntimeException());
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forRead(), handler);

        verify(handler).notFound(new Path("/1"));
    }

    @Test
    public void shouldGetFromTheRepositoryOnceWhenRepeatedlyRead() throws IOException {
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forRead(), handler);
        mountable.with(new Path("/1").forRead(), handler);

        verify(handler, times(2)).found(eq(new Path("/1")), eq(issue), any(Mountable.Control.class));
        verify(repository).getIssues(GHIssueState.OPEN);
        verifyNoMoreInteractions(repository, handler);
    }

    @Test
    public void shouldGetFromTheRepositoryWhenInitiallyOpenedForWrite() throws IOException {
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forWrite(), handler);
        mountable.with(new Path("/1").forWrite(), handler);

        verify(handler, times(2)).found(eq(new Path("/1")), eq(issue), any(Mountable.Control.class));
        verify(repository, times(2)).getIssues(GHIssueState.OPEN);
        verifyNoMoreInteractions(repository, handler);
    }

    @Test
    public void shouldReloadIssueAfterReleasedFromWrite() throws IOException {
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forWrite(), handler);
        mountable.with(new Path("/1").forWrite(), new Mountable.Handler<Object>() {
            @Override public Object found(Path path, Node node, Mountable.Control control) {
                control.release();
                return 0;
            }

            @Override public Object notFound(Path path) {
                throw new UnsupportedOperationException();
            }
        });

        verify(repository, times(2)).getIssues(GHIssueState.OPEN);
        Mockito.reset(repository);

        mountable.with(new Path("/1").forWrite(), handler);
        verify(repository).getIssues(GHIssueState.OPEN);
    }
}
