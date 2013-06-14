package githubfs;

import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;

public class ReadDirHandler implements Mountable.Handler<Integer> {
    private final Path parent;
    private final DirectoryFiller filler;
    private boolean dotDirsWritten = false;
    private int result = -ErrorCodes.ENOENT;

    public ReadDirHandler(Path parent, DirectoryFiller filler) {
        this.parent = parent;
        this.filler = filler;
    }

    @Override public void found(Path path, Node issue) {
        if(parent.isParentOf(path)){
            result = 0;
            if(!dotDirsWritten){
                filler.add(".");
                filler.add("..");
                dotDirsWritten = true;
            }
            filler.add(path.basename());
        }
    }

    @Override public Integer result() {
        return result;
    }
}
