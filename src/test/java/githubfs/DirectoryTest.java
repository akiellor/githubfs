package githubfs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(PowerMockRunner.class)
public class DirectoryTest {
    @Mock Node.Output output;
    @Mock Node.Input input;

    @Test
    public void shouldBeReadableAndWritableDirectory() {
        new Directory().describe(output);

        verify(output, never()).content(any(Content.class));
        verify(output).directory();
    }

    @Test
    public void shouldDoNothingOnUpdate() {
        new Directory().update(input);

        verifyNoMoreInteractions(input);
    }
}
