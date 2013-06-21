package githubfs.handler;

import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenHandlerTest {
    @Mock Node node;
    @Mock Mountable.Control control;

    @Test
    public void shouldReturnENOENTWhenNotFound() {
        Integer result = new OpenHandler().notFound(Path.ROOT);

        assertThat(result, equalTo(-ErrorCodes.ENOENT));
    }

    @Test
    public void shouldOpenFileWithControl() {
        Integer result = new OpenHandler().found(Path.ROOT, node, control);

        verify(control).open();
        assertThat(result, equalTo(0));
    }
}
