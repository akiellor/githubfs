package githubfs;

import net.fusejna.DirectoryFiller;

public class ListingIssueHandler implements Mountable.Handler {
    private final Path parent;
    private final DirectoryFiller filler;
    private boolean dotDirsWritten;

    public ListingIssueHandler(Path parent, DirectoryFiller filler) {
        this.parent = parent;
        this.filler = filler;
        this.dotDirsWritten = false;
    }

    @Override public void found(Path path, Node issue) {
        if(parent.isParentOf(path)){
            if(!dotDirsWritten){
                filler.add(".");
                filler.add("..");
                dotDirsWritten = true;
            }
            filler.add(path.basename());
        }
    }
}
