package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IssueTest {
    @Mock Node.Output file;
    @Mock Content content;

    @Test
    public void shouldWriteToFile() {
        new Issue(content).describe(file);

        verify(file).content(content);
        verify(file).file();
    }
}
