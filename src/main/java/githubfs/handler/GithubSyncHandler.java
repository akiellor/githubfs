package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;
import net.fusejna.StructFuseFileInfo;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;

import java.io.IOException;

public class GithubSyncHandler implements Mountable.Handler<Integer> {
    private final StructFuseFileInfo.FileInfoWrapper info;
    private final GHRepository repository;

    public GithubSyncHandler(StructFuseFileInfo.FileInfoWrapper info, GHRepository repository) {
        this.info = info;
        this.repository = repository;
    }

    @Override public Integer found(final Path path, Node node) {
        if(info.openMode() == StructFuseFileInfo.FileInfoWrapper.OpenMode.READONLY){
            return 0;
        }

        node.update(new Node.Input() {
            @Override public void content(Content content) {
                try {
                    GHIssue issue = repository.getIssue(Integer.valueOf(path.basename()));
                    issue.setBody(content.getContent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return 0;
    }

    @Override public Integer notFound(Path path) {
        return -ErrorCodes.ENOENT;
    }
}
