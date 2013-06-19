package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;

import java.nio.ByteBuffer;

public class TruncateHandler implements Mountable.Handler<Integer> {
    private final long offset;
    private int result = -ErrorCodes.ENOENT;


    public TruncateHandler(long offset) {
        this.offset = offset;
    }

    @Override public void found(Path path, Node node) {
        result = 0;
        node.update(new Node.Input() {
            @Override public void content(Content content) {
                content.write(ByteBuffer.allocate(0), 0, offset);
            }
        });
    }

    @Override public Integer result() {
        return result;
    }
}
