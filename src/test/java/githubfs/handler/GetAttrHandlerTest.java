package githubfs.handler;

import githubfs.Content;
import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;
import net.fusejna.types.TypeUid;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StructStat.StatWrapper.class, TypeUid.class})
public class GetAttrHandlerTest {
    @Mock StructStat.StatWrapper stat;
    @Mock TypeUid uid;
    @Mock Content content;
    private GetAttrHandler.StatOutput output;

    @Before
    public void setUp() {
        when(uid.longValue()).thenReturn(1234L);
        output = new GetAttrHandler.StatOutput(stat, uid);
    }

    @Test
    public void shouldMarkAsFile() {
        output.file();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, true, false);
        verify(stat).uid(1234L);
        verifyNoMoreInteractions(stat);
    }

    @Test
    public void shouldWriteContentSize() {
        when(content.length()).thenReturn(3l);

        output.content(content);

        verify(stat).size(3);
    }

    @Test
    public void shouldMarkAsDirectory() {
        output.directory();

        verify(stat).setMode(TypeMode.NodeType.DIRECTORY, true, false, true);
        verifyNoMoreInteractions(stat);
    }

    @Test
    public void shouldMarkAsExecutable() {
        output.executable();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, false, true);
        verify(stat).uid(1234L);
        verifyNoMoreInteractions(stat);
    }

    @Test
    public void shouldSpecifyUpdatedAt() {
        output.updatedAt(1234000L);

        verify(stat).mtime(1234L);
    }

    @Test
    public void shouldSpecifyCreatedAt() {
        output.createdAt(1234000L);

        verify(stat).ctime(1234L);
    }
}
