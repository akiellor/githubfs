package githubfs;

public interface File {
    void content(String content);

    void file();

    void directory();

    void executable();
}
