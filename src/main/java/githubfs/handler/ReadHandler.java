package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.StructFuseFileInfo;

import java.nio.ByteBuffer;

public class ReadHandler implements Mountable.Handler<Integer> {
    private final ByteBuffer buffer;
    private final int size;
    private final int offset;
    private final StructFuseFileInfo.FileInfoWrapper info;
    private int bytesRead = 0;

    public ReadHandler(ByteBuffer buffer, int size, int offset, StructFuseFileInfo.FileInfoWrapper info){
        this.buffer = buffer;
        this.size = size;
        this.offset = offset;
        this.info = info;
    }

    @Override public void found(Path path, Node node) {
        ReadOutput file = new ReadOutput(buffer, size, offset, info);
        node.describe(file);
        this.bytesRead = file.getBytesRead();
    }

    @Override public Integer result() {
        return bytesRead;
    }

    private static class ReadOutput implements Node.Output {
        private final ByteBuffer buffer;
        private final int size;
        private final int offset;
        private final StructFuseFileInfo.FileInfoWrapper info;
        private int bytesRead;

        public ReadOutput(ByteBuffer buffer, int size, int offset, StructFuseFileInfo.FileInfoWrapper info) {
            this.buffer = buffer;
            this.size = size;
            this.offset = offset;
            this.info = info;
        }

        @Override public void content(Content content) {
            bytesRead = content.read(buffer, size, offset);
            info.flush();
        }

        public int getBytesRead() {
            return bytesRead;
        }

        @Override public void file() {
        }

        @Override public void directory() {
        }

        @Override public void executable() {
        }
    }
}
