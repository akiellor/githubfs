package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.StructFuseFileInfo;

import java.nio.ByteBuffer;

public class WriteHandler implements Mountable.Handler<Integer> {
    private final ByteBuffer buf;
    private final long bufSize;
    private final long writeOffset;
    private final StructFuseFileInfo.FileInfoWrapper info;
    private Integer bytesWritten;

    public WriteHandler(ByteBuffer buf, long bufSize, long writeOffset, StructFuseFileInfo.FileInfoWrapper info) {
        this.buf = buf;
        this.bufSize = bufSize;
        this.writeOffset = writeOffset;
        this.info = info;
    }

    @Override public void found(Path path, Node node) {
        WriteInput input = new WriteInput(buf, bufSize, writeOffset, info);
        node.update(input);
        this.bytesWritten = input.getBytesWritten();
    }

    @Override public Integer result() {
        return bytesWritten;
    }

    public static class WriteInput implements Node.Input {
        private final ByteBuffer byteBuffer;
        private final long bufSize;
        private final long writeOffset;
        private final StructFuseFileInfo.FileInfoWrapper info;
        private Integer bytesWritten;

        public WriteInput(ByteBuffer byteBuffer, long bufSize, long writeOffset, StructFuseFileInfo.FileInfoWrapper info) {
            this.byteBuffer = byteBuffer;
            this.bufSize = bufSize;
            this.writeOffset = writeOffset;
            this.info = info;
        }

        @Override public void content(Content content) {
            this.bytesWritten = content.write(byteBuffer, bufSize, writeOffset);
        }

        public Integer getBytesWritten() {
            return bytesWritten;
        }
    }
}
