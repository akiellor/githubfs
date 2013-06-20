package githubfs;

import githubfs.handler.*;
import net.fusejna.DirectoryFiller;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.util.FuseFilesystemAdapterFull;
import org.kohsuke.github.GHRepository;

import java.nio.ByteBuffer;

public class FileSystem extends FuseFilesystemAdapterFull {
    private final Mountable mountable;
    private final GHRepository repository;

    public FileSystem(Mountable mountable, GHRepository repository) {
        this.mountable = mountable;
        this.repository = repository;
    }

    @Override public int getattr(String path, final StructStat.StatWrapper stat) {
        return mountable.with(new Path(path).forRead(), new GetAttrHandler(stat));
    }

    @Override public int read(String path, final ByteBuffer buffer, long size, long offset, final StructFuseFileInfo.FileInfoWrapper info) {
        return mountable.with(new Path(path).forRead(), new ReadHandler(buffer, (int) size, (int) offset, info));
    }

    @Override public int readdir(String path, final DirectoryFiller filler) {
        return mountable.list(new Path(path), new ReadDirHandler(filler));
    }

    @Override public int write(String path, ByteBuffer buf, long bufSize, long writeOffset, StructFuseFileInfo.FileInfoWrapper info) {
        return mountable.with(new Path(path).forWrite(), new WriteHandler(buf, bufSize, writeOffset, info));
    }

    @Override public int truncate(String path, long offset) {
        return mountable.with(new Path(path).forWrite(), new TruncateHandler(offset));
    }

    @Override public int release(String path, StructFuseFileInfo.FileInfoWrapper info) {
        return mountable.with(new Path(path).forWrite(), new GithubSyncHandler(info, repository));
    }
}
