package githubfs;

import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
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
        return mountable.with(new Path(path), new Mountable.Handler<Integer>() {
            private int result = -ErrorCodes.ENOENT;

            @Override public void found(Path path, Node issue) {
                result = 0;
                issue.describe(new StatFile(stat));
            }

            @Override public Integer result() {
                return result;
            }
        });
    }

    @Override
    public int read(String path, final ByteBuffer buffer, long size, long offset, final StructFuseFileInfo.FileInfoWrapper info) {
        return mountable.with(new Path(path), new WritingIssueHandler(buffer, info));
    }

    @Override
    public int readdir(String path, final DirectoryFiller filler) {
        return mountable.all(new ListingIssueHandler(new Path(path), filler));
    }

    public class WritingIssueHandler implements Mountable.Handler<Integer>{
        private final ByteBuffer buffer;
        private final StructFuseFileInfo.FileInfoWrapper info;
        private int bytesWritten = 0;

        public WritingIssueHandler(ByteBuffer buffer, StructFuseFileInfo.FileInfoWrapper info){
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
}
