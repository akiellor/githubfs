package githubfs.handler;

import githubfs.Content;
import githubfs.Node;
import githubfs.Path;
import net.fusejna.ErrorCodes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TruncateHandlerTest {
    @Mock Path path;

    @Test
    public void shouldTruncateAllDataOnFoundContent() {
        TruncateHandler handler = new TruncateHandler(0);
        final Content content = Content.from("foo");

        handler.found(path, new Node() {
            @Override public void describe(Output output) {
            }

            @Override public void update(Input input) {
                input.content(content);
            }
        });
        Integer result = handler.result();

        assertThat(content.getContent(), equalTo(""));
        assertThat(result, equalTo(0));
    }

    @Test
    public void shouldTruncateFromOffset() {
        TruncateHandler handler = new TruncateHandler(1);
        final Content content = Content.from("foo");

        handler.found(path, new Node() {
            @Override public void describe(Output output) {
            }

            @Override public void update(Input input) {
                input.content(content);
            }
        });
        Integer result = handler.result();

        assertThat(content.getContent(), equalTo("f"));
        assertThat(result, equalTo(0));
    }

    @Test
    public void shouldReturnENOENTWhenNotFound() {
        Integer result = new TruncateHandler(0).result();

        assertThat(result, equalTo(-ErrorCodes.ENOENT));
    }
}
