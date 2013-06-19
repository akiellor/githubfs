package githubfs.handler;

import githubfs.Content;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.StructFuseFileInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StructFuseFileInfo.FileInfoWrapper.class)
public class WriteHandlerTest {
    @Mock ByteBuffer byteBuffer;
    @Mock StructFuseFileInfo.FileInfoWrapper info;
    @Mock Path path;
    @Mock Node node;
    @Mock Content content;

    @Test
    public void shouldUpdateNode() {
        WriteHandler handler = new WriteHandler(byteBuffer, -1, -1, info);

        handler.found(path, node);

        verify(node).update(any(WriteHandler.WriteInput.class));
    }

    @Test
    public void shouldReturnBytesWrittenAsResult() {
        ByteBuffer write = ByteBuffer.allocate(3);
        ByteBuffer read = ByteBuffer.allocate(3);
        write.put(new byte[]{'f', 'o', 'o'});
        write.flip();
        WriteHandler handler = new WriteHandler(write, 3, 0, info);
        final Content content = Content.from("");

        handler.found(path, new Node() {
            @Override public void describe(Output output) {
            }

            @Override public void update(Input input) {
                input.content(content);
            }
        });

        content.read(read, 3, 0);
        assertThat(read.array(), equalTo(new byte[]{'f', 'o', 'o'}));
    }

    @Test
    public void shouldReturnBytesWrittenToContent() {
        when(content.write(any(ByteBuffer.class), anyInt(), anyInt())).thenReturn(5);
        WriteHandler.WriteInput input = new WriteHandler.WriteInput(byteBuffer, 10, 2, info);

        input.content(content);

        assertThat(input.getBytesWritten(), equalTo(5));
        verify(content).write(byteBuffer, 10, 2);
    }
}
