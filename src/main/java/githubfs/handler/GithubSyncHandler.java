package githubfs.handler;

import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;

public class GithubSyncHandler implements Mountable.Handler<Integer> {
    @Override public Integer found(final Path path, Node node, Mountable.Control control) {
        control.release();
        return 0;
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
    }
}
