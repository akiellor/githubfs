package githubfs;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.File;

public class Main {
    public static void main(String... args) {
        try {
            GitHub github = GitHub.connect(args[0], args[1]);
            GHRepository repository = github.getRepository(args[2]);
            final Mountable mountable = new GitHubIssuesMountable(repository);
            FileSystem fileSystem = new FileSystem(mountable);
            fileSystem.mount(new File(args[3]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
