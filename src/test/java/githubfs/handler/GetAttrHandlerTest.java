package githubfs.handler;

import githubfs.Content;
import net.fusejna.StructStat;
import net.fusejna.types.TypeGid;
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
@PrepareForTest({StructStat.StatWrapper.class, TypeUid.class, TypeGid.class})
public class GetAttrHandlerTest {
    @Mock StructStat.StatWrapper stat;
    @Mock TypeUid uid;
    @Mock TypeGid gid;
    @Mock Content content;
    private GetAttrHandler.StatOutput output;

    @Before
    public void setUp() {
        when(uid.longValue()).thenReturn(1234L);
        when(gid.longValue()).thenReturn(5678L);
        output = new GetAttrHandler.StatOutput(stat, uid, gid);
    }

    @Test
    public void shouldMarkAsFile() {
        output.file();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, true, false);
        verify(stat).uid(1234L);
        verify(stat).gid(5678L);
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

        verify(stat).setMode(TypeMode.NodeType.DIRECTORY, true, true, true);
        verify(stat).uid(1234L);
        verify(stat).gid(5678L);
        verifyNoMoreInteractions(stat);
    }

    @Test
    public void shouldMarkAsExecutable() {
        output.executable();

        verify(stat).setMode(TypeMode.NodeType.FILE, true, false, true);
        verify(stat).uid(1234L);
        verify(stat).gid(5678L);
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
