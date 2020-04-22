package hm.binkley;

import lombok.RequiredArgsConstructor;

/** An example Gradle dependent. */
@RequiredArgsConstructor
public class BUnit {
    private final String name;

    /** Returns a trivial message. */
    public String tell() {
        return "Name: " + name;
    }
}
