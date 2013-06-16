package githubfs.handler;

import githubfs.handler.GetAttrHandler;
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
public class StatOutputTest {
    @Mock StructStat.StatWrapper stat;

    @Test
    public void shouldMarkAsFile() {
        new GetAttrHandler.StatOutput(stat).file();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, false, false);
    }

    @Test
    public void shouldWriteContentSize() {
        new GetAttrHandler.StatOutput(stat).content("foo");

        verify(stat).size(3);
    }

    @Test
    public void shouldMarkAsDirectory() {
        new GetAttrHandler.StatOutput(stat).directory();

        verify(stat).setMode(TypeMode.NodeType.DIRECTORY, true, false, true);
    }

    @Test
    public void shouldMarkAsExecutable() {
        new GetAttrHandler.StatOutput(stat).executable();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, false, true);
    }
}
