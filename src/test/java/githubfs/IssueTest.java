package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
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
        new Issue(1234L, content).describe(output);

        verify(output).content(content);
        verify(output).file();
        verify(output).updatedAt(1234L);
        verifyNoMoreInteractions(output);
    }

    @Test
    public void shouldUpdateWithInput() {
        new Issue(0L, content).update(input);

        verify(input).content(content);
        verifyNoMoreInteractions(input);
    }

    @Test
    public void shouldUpdateLastModifiedWhenUpdated() {
        Issue issue = new Issue(0L, content);

        issue.update(input);

        issue.describe(new Node.Output() {
            @Override public void content(Content content) {
            }

            @Override public void file() {
            }

            @Override public void directory() {
            }

            @Override public void executable() {
            }

            @Override public void updatedAt(long time) {
                assertThat(time, greaterThan(0L));
            }
        });
    }

    @Test
    public void shouldBeEqualByValue() {
        assertThat(new Issue(0L, content), equalTo(new Issue(0L, content)));
        assertThat(new Issue(1L, content), not(equalTo(new Issue(0L, content))));
    }
}
