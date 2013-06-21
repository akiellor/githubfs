package githubfs.handler;

import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;

public class OpenHandler implements Mountable.Handler<Integer> {
    @Override public Integer found(Path path, Node node, Mountable.Control control) {
        control.open();
        return 0;
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
    }
}
