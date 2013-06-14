package githubfs;

import net.fusejna.StructFuseFileInfo;

import java.nio.ByteBuffer;

public class ReadHandler implements Mountable.Handler<Integer>{
    private final ByteBuffer buffer;
    private final StructFuseFileInfo.FileInfoWrapper info;
    private int bytesWritten = 0;

    public ReadHandler(ByteBuffer buffer, StructFuseFileInfo.FileInfoWrapper info){
        this.buffer = buffer;
        this.info = info;
    }

    @Override public void found(Path path, Node node) {
        ReadFile file = new ReadFile(buffer, info);
        node.describe(file);
        this.bytesWritten = file.getBytesWritten();
    }

    @Override public Integer result() {
        return bytesWritten;
    }
}
