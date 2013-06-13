package githubfs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Path {
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
        if(path.equals(new Path("/"))){
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
