package githubfs;

import java.nio.ByteBuffer;

public class Content {
    private final String content;

    public Content(String content) {
        this.content = content;
    }

    public long length() {
        return content.length();
    }

    public int write(ByteBuffer buffer) {
        buffer.put(content.getBytes());
        return (int)length();
    }

    public static Content from(String content) {
        return new Content(content);
    }
}
