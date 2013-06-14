package githubfs;

import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StructStat.StatWrapper.class)
public class StatFileTest {
    @Mock StructStat.StatWrapper stat;

    @Test
    public void shouldMarkAsFile() {
        new StatFile(stat).file();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, false, false);
    }

    @Test
    public void shouldWriteContentSize() {
        new StatFile(stat).content("foo");

        verify(stat).size(3);
    }

    @Test
    public void shouldMarkAsDirectory() {
        new StatFile(stat).directory();

        verify(stat).setMode(TypeMode.NodeType.DIRECTORY, true, false, true);
    }

    @Test
    public void shouldMarkAsExecutable() {
        new StatFile(stat).executable();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, false, true);
    }
}
