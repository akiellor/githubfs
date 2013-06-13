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
            issues.with(new Path(path.substring(1)), new Issues.Handler() {
                @Override public void found(Path path, Issue issue) {
                    stat.nlink(1);
                    stat.setMode(TypeMode.NodeType.FILE, true, false, false);
                    stat.size(7);
                }
            });
        }
        return 0;
    }

    @Override
    public int read(String path, final ByteBuffer buffer, long size, long offset, final StructFuseFileInfo.FileInfoWrapper info) {
        issues.with(new Path(path.substring(1)), new Issues.Handler() {
            @Override public void found(Path path, Issue issue) {
                buffer.put("       ".getBytes());
                info.flush();
            }
        });
        return 7;
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
}
