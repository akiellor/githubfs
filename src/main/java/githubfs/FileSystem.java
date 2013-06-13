package githubfs;

import net.fusejna.DirectoryFiller;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;
import net.fusejna.util.FuseFilesystemAdapterFull;

import java.nio.ByteBuffer;

public class FileSystem extends FuseFilesystemAdapterFull {
    private final Issues issues;

    public FileSystem(Issues issues){
        this.issues = issues;
    }

    @Override
    public int getattr(String path, final StructStat.StatWrapper stat) {
        if ("/".equals(path)) {
            stat.nlink(1);
            stat.setMode(TypeMode.NodeType.DIRECTORY, true, false, true);
        } else {
            issues.with(new Path(path), new Issues.Handler() {
                @Override public void found(Path path, Issue issue) {
                    stat.nlink(1);
                    stat.setMode(TypeMode.NodeType.FILE, true, false, false);
                    stat.size(issue.getBody().length());
                }
            });
        }
        return 0;
    }

    @Override
    public int read(String path, final ByteBuffer buffer, long size, long offset, final StructFuseFileInfo.FileInfoWrapper info) {
        WritingIssueHandler handler = new WritingIssueHandler(buffer, info);
        issues.with(new Path(path), handler);
        return handler.bytesWritten;
    }

    @Override
    public int readdir(String path, final DirectoryFiller filler) {
        filler.add(".", "..");
        issues.all(new Issues.Handler() {
            @Override public void found(Path path, Issue issue) {
                filler.add(path.asPathString());
            }
        });
        return 0;
    }

    public class WritingIssueHandler implements Issues.Handler{
        private final ByteBuffer buffer;
        private final StructFuseFileInfo.FileInfoWrapper info;
        private int bytesWritten;

        public WritingIssueHandler(ByteBuffer buffer, StructFuseFileInfo.FileInfoWrapper info){
            this.buffer = buffer;
            this.info = info;
        }

        @Override public void found(Path path, Issue issue) {
            buffer.put(issue.getBody().getBytes());
            info.flush();
            bytesWritten = issue.getBody().length();
        }
    }
}
