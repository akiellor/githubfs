package githubfs;

import net.fusejna.FuseException;

import java.io.File;

public class Main {
    public static void main(String... args) {
        try {
            FileSystem fileSystem = new FileSystem();
            fileSystem.mount(new File(args[0]));
        } catch (FuseException e) {
            throw new RuntimeException();
        }
    }
}
