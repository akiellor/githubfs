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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GitHubIssuesMountableTest {
    @Mock GHRepository repository;
    @Mock Mountable.ListHandler<Integer> listHandler;
    @Mock GHIssue issue;

    @Before
    public void setup(){
        when(issue.getNumber()).thenReturn(1);
        when(issue.getBody()).thenReturn("Foo");
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
                .thenReturn(ImmutableList.of(issue));
        when(listHandler.found(anyMapOf(Path.class, Node.class)))
                .thenReturn(0);

        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        Integer result = mountable.list(Path.ROOT, listHandler);

        assertThat(result, equalTo(0));
        verify(listHandler).found(ImmutableMap.<Path, Node>of(new Path("/1"), new Issue(Content.from("Foo"))));
    }

    @Test
    public void shouldBeNotFoundWhenGetIssuesThrowsException() throws IOException {
        when(repository.getIssues(GHIssueState.OPEN)).thenThrow(new RuntimeException());
        GitHubIssuesMountable mountable = new GitHubIssuesMountable(repository);

        mountable.list(Path.ROOT, listHandler);

        verify(listHandler).notFound(Path.ROOT);
    }
}
