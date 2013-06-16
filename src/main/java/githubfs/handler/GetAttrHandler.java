package githubfs.handler;

import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;
import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;

public class GetAttrHandler implements Mountable.Handler<Integer> {
    private final StructStat.StatWrapper stat;
    private int result;

    public GetAttrHandler(StructStat.StatWrapper stat) {
        this.stat = stat;
        result = -ErrorCodes.ENOENT;
    }

    @Override public void found(Path path, Node issue) {
        result = 0;
        issue.describe(new StatOutput(stat));
    }

    @Override public Integer result() {
        return result;
    }

    public static class StatOutput implements Node.Output {
        private final StructStat.StatWrapper stat;

        public StatOutput(StructStat.StatWrapper stat){
            this.stat = stat;
        }

        @Override public void content(String content) {
            stat.size(content.length());
        }

        @Override public void file() {
            stat.setMode(TypeMode.NodeType.FILE, true, false, false);
        }

        @Override public void directory() {
            stat.setMode(TypeMode.NodeType.DIRECTORY, true, false, true);
        }

        @Override public void executable() {
            stat.setMode(TypeMode.NodeType.FILE, true, false, true);
        }
    }
}
