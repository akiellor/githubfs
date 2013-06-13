package githubfs;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String... args) {
        try {
            final InMemoryIssues issues = new InMemoryIssues();

            GitHub github = GitHub.connect(args[0], args[1]);
            GHRepository repository = github.getRepository(args[2]);
            List<GHIssue> fetchedIssues = repository.getIssues(GHIssueState.OPEN);
            for(GHIssue issue : fetchedIssues){
                issues.put(new Path(String.valueOf(issue.getNumber())), new Issue(issue.getBody()));
            }

            FileSystem fileSystem = new FileSystem(issues);
            fileSystem.mount(new File(args[3]));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
