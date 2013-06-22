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
public class UnlinkHandlerTest {
    @Mock Mountable.Control control;
    @Mock Node node;

    UnlinkHandler handler = new UnlinkHandler();

    @Test
    public void shouldUnlinkNodeWhenFound() {
        Integer result = handler.found(Path.ROOT, node, control);

        assertThat(result, equalTo(0));
        verify(control).unlink();
    }

    @Test
    public void shouldENOENTNodeWhenNotFound() {
        Integer result = handler.notFound(Path.ROOT);

        assertThat(result, equalTo(-ErrorCodes.ENOENT));
    }
}
