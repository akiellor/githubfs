package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class IssueTest {
    @Mock Node.Output output;
    @Mock Node.Input input;
    @Mock Content content;
    @Mock Content anotherContent;

    @Test
    public void shouldDescribeToOutput() {
        new Issue(1234L, 5678L, content).describe(output);

        verify(output).content(content);
        verify(output).file();
        verify(output).createdAt(1234L);
        verify(output).updatedAt(5678L);
        verifyNoMoreInteractions(output);
    }

    @Test
    public void shouldUpdateWithInput() {
        new Issue(0L, 0L, content).update(input);

        verify(input).content(content);
        verifyNoMoreInteractions(input);
    }

    @Test
    public void shouldUpdateLastModifiedWhenUpdated() {
        Issue issue = new Issue(0L, 0L, content);

        issue.update(input);

        issue.describe(output);

        verify(output).updatedAt(argThat(greaterThan(0L)));
    }

    @Test
    public void shouldBeEqualByValue() {
        assertThat(new Issue(0L, 1L, content), equalTo(new Issue(0L, 1L, content)));
        assertThat(new Issue(1L, 2L, content), not(equalTo(new Issue(0L, 1L, content))));
    }
}
