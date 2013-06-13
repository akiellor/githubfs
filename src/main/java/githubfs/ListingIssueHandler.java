package githubfs;

import net.fusejna.DirectoryFiller;

public class ListingIssueHandler implements Issues.Handler {
    private final Path parent;
    private final DirectoryFiller filler;

    public ListingIssueHandler(Path parent, DirectoryFiller filler) {
        this.parent = parent;
        this.filler = filler;
    }

    @Override public void found(Path path, Issue issue) {
        if(parent.isParentOf(path)){
            filler.add(path.basename());
        }
    }
}
