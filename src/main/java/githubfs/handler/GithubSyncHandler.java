package githubfs.handler;

import githubfs.Content;
import githubfs.Mountable;
import githubfs.Node;
import githubfs.Path;
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

    @Override public void found(final Path path, Node node) {
        if(info.openMode() == StructFuseFileInfo.FileInfoWrapper.OpenMode.READONLY){
            return;
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
    }

    @Override public Integer result() {
        return 0;
    }
}
