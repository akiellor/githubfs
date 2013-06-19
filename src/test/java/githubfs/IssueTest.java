package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class IssueTest {
    @Mock Node.Output output;
    @Mock Node.Input input;
    @Mock Content content;

    @Test
    public void shouldDescribeToOutput() {
        new Issue(content).describe(output);

        verify(output).content(content);
        verify(output).file();
        verifyNoMoreInteractions(output);
    }


    @Test
    public void shouldUpdateWithInput() {
        new Issue(content).update(input);

        verify(input).content(content);
        verifyNoMoreInteractions(input);
    }
}
