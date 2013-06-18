package githubfs;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class ContentTest {
    @Test
    public void shouldHaveLength() {
        assertEquals(3l, Content.from("foo").length());
    }

    @Test
    public void shouldReadToByteBuffer() {
        ByteBuffer out = ByteBuffer.allocate(3);

        int bytesRead = Content.from("foo").read(out, 3, 0);

        assertThat(bytesRead, equalTo(3));
        assertThat(out.array(), equalTo(new byte[]{'f', 'o', 'o'}));
    }

    @Test
    public void shouldReadPartialToByteBuffer() {
        ByteBuffer out = ByteBuffer.allocate(2);

        int bytesRead = Content.from("foo").read(out, 2, 1);

        assertThat(bytesRead, equalTo(2));
        assertThat(out.array(), equalTo(new byte[]{'o', 'o'}));
    }

    @Test
    public void shouldReadPartialToByteBufferWithBytesRemaining() {
        ByteBuffer out = ByteBuffer.allocate(2);

        int bytesRead = Content.from("foo").read(out, 2, 0);

        assertThat(bytesRead, equalTo(2));
        assertThat(out.array(), equalTo(new byte[]{'f', 'o'}));
    }

    @Test
    public void shouldReadAsManyBytesWithinSize() {
        ByteBuffer out = ByteBuffer.allocate(2);

        int bytesRead = Content.from("foo").read(out, 20, 1);

        assertThat(bytesRead, equalTo(2));
        assertThat(out.array(), equalTo(new byte[]{'o', 'o'}));
    }
}
