package githubfs;

import net.fusejna.StructFuseFileInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StructFuseFileInfo.FileInfoWrapper.class)
public class FileSystemTest {
    @Mock StructFuseFileInfo.FileInfoWrapper info;
    @Mock Mountable mountable;

    @Before
    public void setup(){
        when(mountable.with(any(Usage.class), any(Mountable.Handler.class))).thenReturn(0);
    }

    @Test
    public void shouldUsageFileForWriteWhenOpenedForReadWrite() {
        when(info.openMode()).thenReturn(StructFuseFileInfo.FileInfoWrapper.OpenMode.READWRITE);
        FileSystem fileSystem = new FileSystem(mountable);

        fileSystem.release("/", info);

        verify(mountable).with(eq(Path.ROOT.forWrite()), any(Mountable.Handler.class));
    }

    @Test
    public void shouldUsageFileForWriteWhenOpenedForWrite() {
        when(info.openMode()).thenReturn(StructFuseFileInfo.FileInfoWrapper.OpenMode.WRITEONLY);
        FileSystem fileSystem = new FileSystem(mountable);

        fileSystem.release("/", info);

        verify(mountable).with(eq(Path.ROOT.forWrite()), any(Mountable.Handler.class));
    }

    @Test
    public void shouldUsageFileForReadWhenOpenedForRead() {
        when(info.openMode()).thenReturn(StructFuseFileInfo.FileInfoWrapper.OpenMode.READONLY);
        FileSystem fileSystem = new FileSystem(mountable);

        fileSystem.release("/", info);

        verify(mountable).with(eq(Path.ROOT.forRead()), any(Mountable.Handler.class));
    }
}
