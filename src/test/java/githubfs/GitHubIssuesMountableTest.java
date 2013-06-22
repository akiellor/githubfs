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
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Date;

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
    @Mock GHIssue ghFoo;
    @Mock GHIssue ghFooBar;
    @Mock Node node;

    Issue foo;
    Issue fooBar;

    @Before
    public void setup() throws IOException {
        when(ghFoo.getCreatedAt()).thenReturn(new Date(1234L));
        when(ghFoo.getUpdatedAt()).thenReturn(new Date(5678L));
        when(ghFoo.getNumber()).thenReturn(1);
        when(ghFoo.getTitle()).thenReturn("Foo");
        when(ghFoo.getBody()).thenReturn("Body");
        when(ghFooBar.getCreatedAt()).thenReturn(new Date(1234L));
        when(ghFooBar.getUpdatedAt()).thenReturn(new Date(5678L));
        when(ghFooBar.getNumber()).thenReturn(1);
        when(ghFooBar.getTitle()).thenReturn("FooBar");
        when(ghFooBar.getBody()).thenReturn("Body");
        when(repository.getIssues(GHIssueState.OPEN)).thenReturn(ImmutableList.of(ghFoo));
        when(repository.getIssue(1)).thenReturn(ghFoo);
        foo = new Issue(1234L, 5678L, Content.from("Foo\nBody"));
        fooBar = new Issue(1234L, 5678L, Content.from("FooBar\nBody"));
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
                .thenReturn(ImmutableList.of(ghFoo));
        when(listHandler.found(anyMapOf(Path.class, Node.class)))
                .thenReturn(0);

        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        Integer result = mountable.list(Path.ROOT, listHandler);

        assertThat(result, equalTo(0));
        verify(listHandler).found(ImmutableMap.<Path, Node>of(new Path("/1"), foo));
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
        when(repository.getIssues(GHIssueState.OPEN)).thenReturn(ImmutableList.of(ghFoo));
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forRead(), handler);

        verify(handler).found(eq(new Path("/1")), eq(foo), any(Mountable.Control.class));
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
    public void shouldNotRefreshIssueOnceOpened() throws IOException {
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forWrite(), new Mountable.Handler<Object>() {
            @Override public Object found(Path path, Node node, Mountable.Control control) {
                control.open();
                return 0;
            }

            @Override public Object notFound(Path path) {
                throw new UnsupportedOperationException();
            }
        });

        when(repository.getIssues(GHIssueState.OPEN)).thenReturn(ImmutableList.of(ghFooBar));
        when(repository.getIssue(1)).thenReturn(ghFooBar);

        mountable.with(new Path("/1").forRead(), handler);

        verify(handler).found(eq(new Path("/1")), eq(foo), any(Mountable.Control.class));
        verifyNoMoreInteractions(handler);
    }

    @Test
    public void shouldRefreshIssueOnceReleased() throws IOException {
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forWrite(), new Mountable.Handler<Object>() {
            @Override public Object found(Path path, Node node, Mountable.Control control) {
                control.open();
                return 0;
            }

            @Override public Object notFound(Path path) {
                throw new UnsupportedOperationException();
            }
        });

        when(repository.getIssues(GHIssueState.OPEN)).thenReturn(ImmutableList.of(ghFooBar));
        when(repository.getIssue(1)).thenReturn(ghFooBar);

        mountable.with(new Path("/1").forWrite(), new Mountable.Handler<Object>() {
            @Override public Object found(Path path, Node node, Mountable.Control control) {
                control.release();
                return 0;
            }

            @Override public Object notFound(Path path) {
                throw new UnsupportedOperationException();
            }
        });

        mountable.with(new Path("/1").forRead(), handler);

        verify(handler).found(eq(new Path("/1")), eq(fooBar), any(Mountable.Control.class));
        verifyNoMoreInteractions(handler);
    }

    @Test
    public void shouldCloseIssueOnUnlink() throws IOException {
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.with(new Path("/1").forWrite(), new Mountable.Handler<Object>() {
            @Override public Object found(Path path, Node node, Mountable.Control control) {
                control.unlink();
                return null;
            }

            @Override public Object notFound(Path path) {
                throw new UnsupportedOperationException();
            }
        });

        when(repository.getIssues(GHIssueState.OPEN)).thenReturn(Lists.<GHIssue>newArrayList());

        mountable.with(new Path("/1").forWrite(), handler);

        verify(handler).notFound(new Path("/1"));
        verify(ghFoo).close();
        verifyNoMoreInteractions(handler);
    }
}
