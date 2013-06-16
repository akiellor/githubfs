package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class DirectoryTest {
    @Mock Node.Output file;

    @Test
    public void shouldBeReadableAndWritableDirectory() {
        new Directory().describe(file);

        verify(file, never()).content(any(Content.class));
        verify(file).directory();
    }
}
