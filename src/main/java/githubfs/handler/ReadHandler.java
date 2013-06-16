package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.StructFuseFileInfo;

import java.nio.ByteBuffer;

public class ReadHandler implements Mountable.Handler<Integer> {
    private final ByteBuffer buffer;
    private final StructFuseFileInfo.FileInfoWrapper info;
    private int bytesWritten = 0;

    public ReadHandler(ByteBuffer buffer, StructFuseFileInfo.FileInfoWrapper info){
        this.buffer = buffer;
        this.info = info;
    }

    @Override public void found(Path path, Node node) {
        ReadOutput file = new ReadOutput(buffer, info);
        node.describe(file);
        this.bytesWritten = file.getBytesWritten();
    }

    @Override public Integer result() {
        return bytesWritten;
    }

    private static class ReadOutput implements Node.Output {
        private final ByteBuffer buffer;
        private final StructFuseFileInfo.FileInfoWrapper info;
        private int bytesWritten;

        public ReadOutput(ByteBuffer buffer, StructFuseFileInfo.FileInfoWrapper info) {
            this.buffer = buffer;
            this.info = info;
        }

        @Override public void content(Content content) {
            bytesWritten = content.write(buffer);
            info.flush();
        }

        public int getBytesWritten() {
            return bytesWritten;
        }

        @Override public void file() {
        }

        @Override public void directory() {
        }

        @Override public void executable() {
        }
    }
}
