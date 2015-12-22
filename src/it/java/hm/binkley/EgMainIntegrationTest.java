package hm.binkley;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public final class EgMainIntegrationTest {
    @Test
    public void shouldRunSlow()
            throws InterruptedException {
        new EgMain().slow();
        assertThat(2).isEqualTo(2);
    }
}
