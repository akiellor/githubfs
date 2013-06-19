package githubfs;

import githubfs.handler.*;
import net.fusejna.DirectoryFiller;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.util.FuseFilesystemAdapterFull;

import java.nio.ByteBuffer;

public class FileSystem extends FuseFilesystemAdapterFull {
    private final Mountable mountable;

    public FileSystem(Mountable mountable) {
        this.mountable = mountable;
    }

    @Override public int getattr(String path, final StructStat.StatWrapper stat) {
        return mountable.with(new Path(path), new GetAttrHandler(stat));
    }

    @Override public int read(String path, final ByteBuffer buffer, long size, long offset, final StructFuseFileInfo.FileInfoWrapper info) {
        return mountable.with(new Path(path), new ReadHandler(buffer, (int) size, (int) offset, info));
    }

    @Override public int readdir(String path, final DirectoryFiller filler) {
        return mountable.all(new ReadDirHandler(new Path(path), filler));
    }

    @Override public int write(String path, ByteBuffer buf, long bufSize, long writeOffset, StructFuseFileInfo.FileInfoWrapper info) {
        return mountable.with(new Path(path), new WriteHandler(buf, bufSize, writeOffset, info));
    }

    @Override public int truncate(String path, long offset) {
        return mountable.with(new Path(path), new TruncateHandler(offset));
    }
}
