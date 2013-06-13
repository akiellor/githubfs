package githubfs;

public class ListingIssueHandler implements Issues.Handler {
    private final Path parent;
    private final DirectoryListing listing;

    public ListingIssueHandler(Path parent, DirectoryListing listing) {
        this.parent = parent;
        this.listing = listing;
    }

    @Override public void found(Path path, Issue issue) {
        if(parent.isParentOf(path)){
            listing.add(path.basename());
        }
    }
}
