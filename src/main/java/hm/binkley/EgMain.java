package hm.binkley;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * {@code EgMain} <b>needs documentation</b>.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation.
 */
public final class EgMain {
    public static void main(final String... args) {
    }

    public void fast() {}

    public void slow()
            throws InterruptedException {
        SECONDS.sleep(1L);
    }
}
