package hm.binkley;

import org.junit.jupiter.api.Test;

import static hm.binkley.BeAssertive.assertLittle;
import static hm.binkley.BeIttyAssertive.assertNothing;

class AIttyTest {
    @Test
    void shouldRhumba() {
        final var lib = new AUnit();

        assertLittle(lib);
        assertNothing();
    }
}
