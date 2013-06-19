package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;
import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;

public class GetAttrHandler implements Mountable.Handler<Integer> {
    private final StructStat.StatWrapper stat;

    public GetAttrHandler(StructStat.StatWrapper stat) {
        this.stat = stat;
    }

    @Override public Integer found(Path path, Node issue) {
        issue.describe(new StatOutput(stat));
        return 0;
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
    }

    public static class StatOutput implements Node.Output {
        private final StructStat.StatWrapper stat;

        public StatOutput(StructStat.StatWrapper stat){
            this.stat = stat;
        }

        @Override public void content(Content content) {
            stat.size(content.length());
        }

        @Override public void file() {
            stat.setMode(TypeMode.NodeType.FILE, true, true, false);
        }

        @Override public void directory() {
            stat.setMode(TypeMode.NodeType.DIRECTORY, true, false, true);
        }

        @Override public void executable() {
            stat.setMode(TypeMode.NodeType.FILE, true, false, true);
        }
    }
}
