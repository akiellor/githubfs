package githubfs;

import net.fusejna.StructFuseFileInfo;

import java.nio.ByteBuffer;

public class ReadOutput implements Node.Output {
    private final ByteBuffer buffer;
    private final StructFuseFileInfo.FileInfoWrapper info;
    private int bytesWritten;

    public ReadOutput(ByteBuffer buffer, StructFuseFileInfo.FileInfoWrapper info) {
        this.buffer = buffer;
        this.info = info;
    }

    @Override public void content(String content) {
        byte[] bytes = content.getBytes();
        buffer.put(bytes);
        info.flush();
        bytesWritten = bytes.length;
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
