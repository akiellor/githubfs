package githubfs;

import java.nio.ByteBuffer;

public class Content {
    private String content;

    public static Content from(String content) {
        return new Content(content);
    }

    public Content(String content) {
        this.content = content;
    }

    public long length() {
        return content.length();
    }

    public String getContent() {
        return content;
    }

    public int read(ByteBuffer out, int size, int offset) {
        int bytesToRead = Math.min(size, (content.length() - offset));
        out.put(content.substring(offset, offset + bytesToRead).getBytes());
        return bytesToRead;
    }

    public int write(ByteBuffer byteBuffer, long size, long offset) {
        content = content.substring(0, (int)offset) + read(byteBuffer, (int)size);
        return (int) size;
    }

    private String read(ByteBuffer byteBuffer, int size){
        byte[] result = new byte[size];
        for(int i = 0; i < size; i++){
            result[i] = byteBuffer.get();
        }
        return new String(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content1 = (Content) o;

        if (content != null ? !content.equals(content1.content) : content1.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("Content{");
        sb.append("content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getTitle() {
        return content.split("\\n")[0];
    }

    public String getBody() {
        String[] parts = content.split("\\n", 2);
        return parts.length > 1 ? parts[1] : "";
    }
}
