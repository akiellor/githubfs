package githubfs.handler;

import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;

public class UnlinkHandler implements Mountable.Handler<Integer> {
    @Override public Integer found(Path path, Node node, Mountable.Control control) {
        control.unlink();
        return 0;
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
    }
}
