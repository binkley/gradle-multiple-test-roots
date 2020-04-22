package hm.binkley;

/** An example dependency between Gradle modules. */
public class AUnit {
    /** Calls the dependency. */
    public String doIt(final BUnit dependable) {
        return dependable.tell();
    }
}
