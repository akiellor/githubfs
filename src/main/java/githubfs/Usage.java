package githubfs;

public abstract class Usage {
    private final Path path;
    private final Mode mode;

    public static Usage read(Path path){
        return new Read(path);
    }

    public static Usage write(Path path){
        return new Write(path);
    }

    public Path path() {
        return path;
    }

    public boolean isWrite() {
        return mode == Mode.WRITE;
    }

    private enum Mode{
        READ, WRITE
    }

    protected Usage(Path path, Mode mode) {
        this.path = path;
        this.mode = mode;
    }

    private static class Write extends Usage{
        public Write(Path path){
            super(path, Mode.WRITE);
        }
    }

    private static class Read extends Usage{
        public Read(Path path){
            super(path, Mode.READ);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usage usage = (Usage) o;

        if (mode != usage.mode) return false;
        if (path != null ? !path.equals(usage.path) : usage.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        return result;
    }
}
