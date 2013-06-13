package githubfs;

import net.fusejna.DirectoryFiller;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;
import net.fusejna.util.FuseFilesystemAdapterFull;

import java.nio.ByteBuffer;

public class FileSystem extends FuseFilesystemAdapterFull {
    @Override
    public int getattr(String path, StructStat.StatWrapper stat) {
        if ("/".equals(path)) {
            stat.nlink(1);
            stat.setMode(TypeMode.NodeType.DIRECTORY, true, false, true);
        } else if ("/foo".equals(path)) {
            stat.nlink(1);
            stat.setMode(TypeMode.NodeType.FILE, true, false, false);
            stat.size(7);
        }
        return 0;
    }

    @Override
    public int read(String path, ByteBuffer buffer, long size, long offset, StructFuseFileInfo.FileInfoWrapper info) {
        if ("/foo".equals(path)) {
            buffer.put("       ".getBytes());
            info.flush();
        }
        return 7;
    }

    @Override
    public int readdir(String path, DirectoryFiller filler) {
        filler.add(".", "..", "foo");
        return 0;
    }
}
