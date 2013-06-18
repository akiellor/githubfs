package githubfs;

import java.nio.ByteBuffer;

public class Content {
    private final String content;

    public static Content from(String content) {
        return new Content(content);
    }

    public Content(String content) {
        this.content = content;
    }

    public long length() {
        return content.length();
    }

    public int read(ByteBuffer out, int size, int offset) {
        int bytesToRead = Math.min(size, (content.length() - offset));
        out.put(content.substring(offset, offset + bytesToRead).getBytes());
        return bytesToRead;
    }
}
