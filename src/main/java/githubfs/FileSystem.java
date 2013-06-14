package githubfs;

import net.fusejna.DirectoryFiller;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;
import net.fusejna.util.FuseFilesystemAdapterFull;

import java.nio.ByteBuffer;

public class FileSystem extends FuseFilesystemAdapterFull {
    private final Mountable mountable;

    public FileSystem(Mountable mountable){
        this.mountable = mountable;
    }

    @Override
    public int getattr(String path, final StructStat.StatWrapper stat) {
        if ("/".equals(path)) {
            stat.nlink(1);
            stat.setMode(TypeMode.NodeType.DIRECTORY, true, false, true);
        } else {
            mountable.with(new Path(path), new Mountable.Handler() {
                 @Override public void found(Path path, Node issue) {
                    issue.describe(new StatFile(stat));
                }
            });
        }
        return 0;
    }

    @Override
    public int read(String path, final ByteBuffer buffer, long size, long offset, final StructFuseFileInfo.FileInfoWrapper info) {
        WritingIssueHandler handler = new WritingIssueHandler(buffer, info);
        mountable.with(new Path(path), handler);
        return handler.bytesWritten;
    }

    @Override
    public int readdir(String path, final DirectoryFiller filler) {
        filler.add(".", "..");
        mountable.all(new ListingIssueHandler(new Path(path), filler));
        return 0;
    }

    public class WritingIssueHandler implements Mountable.Handler{
        private final ByteBuffer buffer;
        private final StructFuseFileInfo.FileInfoWrapper info;
        private int bytesWritten;

        public WritingIssueHandler(ByteBuffer buffer, StructFuseFileInfo.FileInfoWrapper info){
            this.buffer = buffer;
            this.info = info;
        }

        @Override public void found(Path path, Node node) {
            ReadFile file = new ReadFile(buffer, info);
            node.describe(file);
            this.bytesWritten = file.getBytesWritten();
        }
    }
}
