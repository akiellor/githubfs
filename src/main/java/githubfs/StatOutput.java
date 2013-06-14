package githubfs;

import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;

public class StatOutput implements Node.Output {
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
