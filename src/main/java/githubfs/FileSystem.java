package githubfs;

import githubfs.handler.GetAttrHandler;
import githubfs.handler.ReadDirHandler;
import githubfs.handler.ReadHandler;
import net.fusejna.DirectoryFiller;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.util.FuseFilesystemAdapterFull;

import java.nio.ByteBuffer;

public class FileSystem extends FuseFilesystemAdapterFull {
    private final Mountable mountable;

    public FileSystem(Mountable mountable){
        this.mountable = mountable;
    }

    @Override
    public int getattr(String path, final StructStat.StatWrapper stat) {
        return mountable.with(new Path(path), new GetAttrHandler(stat));
    }

    @Override
    public int read(String path, final ByteBuffer buffer, long size, long offset, final StructFuseFileInfo.FileInfoWrapper info) {
        return mountable.with(new Path(path), new ReadHandler(buffer, info));
    }

    @Override
    public int readdir(String path, final DirectoryFiller filler) {
        return mountable.all(new ReadDirHandler(new Path(path), filler));
    }
}
