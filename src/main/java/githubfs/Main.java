package githubfs;

import net.fusejna.FuseException;

import java.io.File;

public class Main {
    public static void main(String... args) {
        try {
            InMemoryIssues issues = new InMemoryIssues();
            issues.put(new Path("foo"), new Issue());
            issues.put(new Path("bar"), new Issue());
            FileSystem fileSystem = new FileSystem(issues);
            fileSystem.mount(new File(args[0]));
        } catch (FuseException e) {
            throw new RuntimeException();
        }
    }
}
