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
    public void shouldMarkAsReadable() {
        new StatFile(stat).readable();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, false, false);
    }

    @Test
    public void shouldWriteContentSize() {
        new StatFile(stat).content("foo");

        verify(stat).size(3);
    }
}
