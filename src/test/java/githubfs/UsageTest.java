package githubfs;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class UsageTest {
    @Test
    public void shouldWriteUsageForSamePathShouldBeEqual() {
        assertThat(Usage.read(Path.ROOT), equalTo(Usage.read(Path.ROOT)));
        assertThat(Usage.read(Path.ROOT), not(equalTo(Usage.read(new Path("/foo")))));

        assertThat(Usage.write(Path.ROOT), equalTo(Usage.write(Path.ROOT)));
        assertThat(Usage.write(Path.ROOT), not(equalTo(Usage.write(new Path("/foo")))));

        assertThat(Usage.read(Path.ROOT), not(equalTo(Usage.write(new Path("/foo")))));
        assertThat(Usage.read(Path.ROOT), not(equalTo(Usage.write(Path.ROOT))));
    }

    @Test
    public void shouldBeWrite() {
        assertTrue(Usage.write(Path.ROOT).isWrite());
    }
}
