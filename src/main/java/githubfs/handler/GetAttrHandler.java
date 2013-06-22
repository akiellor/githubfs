package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;
import net.fusejna.StructStat;
import net.fusejna.types.TypeGid;
import net.fusejna.types.TypeMode;
import net.fusejna.types.TypeUid;

public class GetAttrHandler implements Mountable.Handler<Integer> {
    private final StructStat.StatWrapper stat;
    private final TypeUid uid;
    private final TypeGid gid;

    public GetAttrHandler(StructStat.StatWrapper stat, TypeUid uid, TypeGid gid) {
        this.stat = stat;
        this.uid = uid;
        this.gid = gid;
    }

    @Override public Integer found(Path path, Node issue, Mountable.Control control) {
        issue.describe(new StatOutput(stat, uid, gid));
        return 0;
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
    }

    public static class StatOutput implements Node.Output {
        private final StructStat.StatWrapper stat;
        private final TypeUid uid;
        private final TypeGid gid;

        public StatOutput(StructStat.StatWrapper stat, TypeUid uid, TypeGid gid){
            this.stat = stat;
            this.uid = uid;
            this.gid = gid;
        }

        @Override public void content(Content content) {
            stat.size(content.length());
        }

        @Override public void file() {
            stat.setMode(TypeMode.NodeType.FILE, true, true, false);
            stat.uid(uid.longValue());
            stat.gid(gid.longValue());
        }

        @Override public void directory() {
            stat.setMode(TypeMode.NodeType.DIRECTORY, true, true, true);
            stat.uid(uid.longValue());
            stat.gid(gid.longValue());
        }

        @Override public void executable() {
            stat.setMode(TypeMode.NodeType.FILE, true, false, true);
            stat.uid(uid.longValue());
            stat.gid(gid.longValue());
        }

        @Override public void updatedAt(Long time) {
            stat.mtime(time / 1000);
        }

        @Override public void createdAt(Long time) {
            stat.ctime(time / 1000);
        }
    }
}
