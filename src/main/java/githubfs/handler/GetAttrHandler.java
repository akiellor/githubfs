package githubfs.handler;

import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import githubfs.StatOutput;
import net.fusejna.ErrorCodes;
import net.fusejna.StructStat;

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
}
