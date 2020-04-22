package hm.binkley;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BUnitTest {
    @Test
    void shouldTell() {
        final var name = "BOB";
        final var lib = new BUnit(name);

        assertThat(lib.tell()).contains(name);
    }
}
