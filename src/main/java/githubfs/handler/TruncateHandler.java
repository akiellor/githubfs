package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;

import java.nio.ByteBuffer;

public class TruncateHandler implements Mountable.Handler<Integer> {
    private final long offset;

    public TruncateHandler(long offset) {
        this.offset = offset;
    }

    @Override public Integer found(Path path, Node node) {
        node.update(new Node.Input() {
            @Override public void content(Content content) {
                content.write(ByteBuffer.allocate(0), 0, offset);
            }
        });
        return 0;
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
    }
}
