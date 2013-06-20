package githubfs;

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Path {
    public static final Path ROOT = new Path("/");
    public static final Predicate<Path> IS_ROOT = new Predicate<Path>() {
        @Override public boolean apply(Path input) {
            return ROOT.equals(input);
        }
    };

    private final LinkedList<String> parts;

    public Path(String parts) {
        this(dropEmpty(Arrays.asList(parts.substring(1).split("/"))));
    }

    private static List<String> dropEmpty(List<String> parts) {
        List<String> result = new ArrayList<String>();
        for(String part : parts){
            if(!"".equals(part)){
                result.add(part);
            }
        }
        return result;
    }

    private Path(List<String> parts) {
        this.parts = new LinkedList<String>(parts);
    }

    public String basename() {
        return parts.getLast();
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("Path{");
        sb.append("parts=").append(parts);
        sb.append('}');
        return sb.toString();
    }

    public boolean isParentOf(Path path) {
        if(path.equals(Path.ROOT)){
            return false;
        }
        return path.parent().equals(this);
    }

    public Path parent() {
        if(parts.size() < 1){
            throw new IllegalStateException();
        }
        return new Path(parts.subList(0, parts.size() - 1));
    }

    public List<Path> ancestors() {
        if(this.equals(Path.ROOT)){
            return Arrays.asList();
        }else{
            ArrayList<Path> paths = new ArrayList<Path>();
            paths.add(parent());
            paths.addAll(parent().ancestors());
            return paths;
        }
    }

    public boolean isAncestorOf(Path path) {
        return path.ancestors().contains(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Path path = (Path) o;

        if (parts != null ? !parts.equals(path.parts) : path.parts != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return parts != null ? parts.hashCode() : 0;
    }
}
