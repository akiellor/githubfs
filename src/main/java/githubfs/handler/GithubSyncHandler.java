package githubfs.handler;

import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;
import net.fusejna.StructFuseFileInfo;
import org.kohsuke.github.GHRepository;

public class GithubSyncHandler implements Mountable.Handler<Integer> {
    private final StructFuseFileInfo.FileInfoWrapper info;
    private final GHRepository repository;

    public GithubSyncHandler(StructFuseFileInfo.FileInfoWrapper info, GHRepository repository) {
        this.info = info;
        this.repository = repository;
    }

    @Override public Integer found(final Path path, Node node, Mountable.Control control) {
        control.release();
        return 0;
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
    }
}
