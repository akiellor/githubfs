package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IssueTest {
    @Mock File file;

    @Test
    public void shouldWriteToFile() {
        new Issue("foo").describe(file);

        verify(file).content("foo");
        verify(file).file();
    }
}
