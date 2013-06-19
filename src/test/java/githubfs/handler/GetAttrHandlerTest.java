package githubfs.handler;

import githubfs.Content;
import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StructStat.StatWrapper.class)
public class GetAttrHandlerTest {
    @Mock StructStat.StatWrapper stat;
    @Mock Content content;

    @Test
    public void shouldMarkAsFile() {
        new GetAttrHandler.StatOutput(stat).file();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, true, false);
    }

    @Test
    public void shouldWriteContentSize() {
        when(content.length()).thenReturn(3l);

        new GetAttrHandler.StatOutput(stat).content(content);

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
