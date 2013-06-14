package githubfs;

import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;

public class StatFile implements File{
    private final StructStat.StatWrapper stat;

    public StatFile(StructStat.StatWrapper stat){
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
}
