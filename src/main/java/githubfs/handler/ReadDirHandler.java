package githubfs.handler;

import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;

import java.util.Map;

public class ReadDirHandler implements Mountable.ListHandler<Integer> {
    private final DirectoryFiller filler;

    public ReadDirHandler(DirectoryFiller filler) {
        this.filler = filler;
    }

    @Override public Integer found(Map<Path, Node> entries) {
        filler.add(".");
        filler.add("..");
        for (Path path : entries.keySet()) {
            filler.add(path.basename());
        }
        return 0;
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
    }
}
