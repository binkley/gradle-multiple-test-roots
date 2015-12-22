package hm.binkley;

import org.junit.Test;

public final class EgMainIntegrationTest {
    @Test
    public void shouldRunSlow()
            throws InterruptedException {
        new EgMain().slow();
    }
}
