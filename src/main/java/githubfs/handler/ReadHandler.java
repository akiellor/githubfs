package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;
import net.fusejna.StructFuseFileInfo;

import java.nio.ByteBuffer;

public class ReadHandler implements Mountable.Handler<Integer> {
    private final ByteBuffer buffer;
    private final int size;
    private final int offset;
    private final StructFuseFileInfo.FileInfoWrapper info;

    public ReadHandler(ByteBuffer buffer, int size, int offset, StructFuseFileInfo.FileInfoWrapper info){
        this.buffer = buffer;
        this.size = size;
        this.offset = offset;
        this.info = info;
    }

    @Override public Integer found(Path path, Node node) {
        ReadOutput file = new ReadOutput(buffer, size, offset, info);
        node.describe(file);
        return file.getBytesRead();
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
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
