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
            final InMemoryMountable issues = new InMemoryMountable();

            GitHub github = GitHub.connect(args[0], args[1]);
            GHRepository repository = github.getRepository(args[2]);
            List<GHIssue> fetchedIssues = repository.getIssues(GHIssueState.OPEN);
            for(GHIssue issue : fetchedIssues){
                issues.put(new Path("/issues/" + String.valueOf(issue.getNumber())), new Issue(Content.from(issue.getBody())));
            }

            issues.put(new Path("/foo/bar"), new Node() {
                @Override public void describe(Output output) {
                    output.content(Content.from("bar"));
                    output.file();
                }
            });

            issues.put(new Path("/foo/baz"), new Node() {
                @Override public void describe(Output output) {
                    output.content(Content.from("#!/bin/sh -e\n\necho foo"));
                    output.executable();
                }
            });

            FileSystem fileSystem = new FileSystem(issues);
            fileSystem.mount(new File(args[3]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
