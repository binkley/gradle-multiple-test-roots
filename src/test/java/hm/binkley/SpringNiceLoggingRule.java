package hm.binkley;

import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.RuleChain.outerRule;

/** @todo De-springify */
public final class SpringNiceLoggingRule
        implements TestRule {
    private static final Pattern NEWLINE = compile("\n");

    private final SystemOutRule sout = new SystemOutRule().
            enableLog().
            muteForSuccessfulTests();
    private final SystemErrRule serr = new SystemErrRule().
            enableLog();

    private final RuleChain delegate;

    public SpringNiceLoggingRule() {
        delegate = outerRule(new CheckLogging()).
                around(sout).
                around(serr);
    }

    @Override
    public Statement apply(final Statement base,
            final Description description) {
        return delegate.apply(base, description);
    }

    private class CheckLogging
            extends ExternalResource {
        @Override
        protected void after() {
            final String cleanSerr = serr.getLogWithNormalizedLineSeparator();
            assertThat(NEWLINE.splitAsStream(cleanSerr).
                    collect(toList())).
                    isEmpty();

            final String cleanSout = sout.getLogWithNormalizedLineSeparator();
            assertThat(NEWLINE.splitAsStream(cleanSout).
                    map(LogLine::new).
                    filter(LogLine::problematic).
                    collect(toList())).
                    isEmpty();
        }
    }
}
