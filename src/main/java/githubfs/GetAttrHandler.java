package githubfs;

import net.fusejna.ErrorCodes;
import net.fusejna.StructStat;

class GetAttrHandler implements Mountable.Handler<Integer> {
    private final StructStat.StatWrapper stat;
    private int result;

    public GetAttrHandler(StructStat.StatWrapper stat) {
        this.stat = stat;
        result = -ErrorCodes.ENOENT;
    }

    @Override public void found(Path path, Node issue) {
        result = 0;
        issue.describe(new StatFile(stat));
    }

    @Override public Integer result() {
        return result;
    }
}
