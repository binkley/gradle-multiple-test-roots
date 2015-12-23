package hm.binkley;

import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.boot.logging.LogLevel;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.RuleChain.outerRule;
import static org.springframework.boot.logging.LogLevel.INFO;

public final class NiceLoggingRule
        implements TestRule {
    private static final Pattern NEWLINE = compile("\n");

    private final SystemOutRule sout = new SystemOutRule().
            enableLog().
            muteForSuccessfulTests();
    private final SystemErrRule serr = new SystemErrRule().
            enableLog();

    private final Pattern logLinePattern;
    private final RuleChain delegate;

    public NiceLoggingRule(final String logLinePattern) {
        this.logLinePattern = compile(logLinePattern);
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

    private final class LogLine {
        @Nonnull
        private final String line;
        @Nonnull
        private final LogLevel level;

        private LogLine(@Nonnull final String line) {
            try {
                final Matcher match = logLinePattern.matcher(line);
                if (!match.find()) // Not match! Ignore trailing CR?NL
                    throw new AssertionError(
                            format("Log line does not match expected pattern (%s): %s",
                                    logLinePattern.pattern(), line));
                this.line = line;
                level = LogLevel.valueOf(match.group("level"));
            } catch (final IllegalArgumentException e) {
                throw new AssertionError(
                        format("Log line with unknown log level: %s", line));
            }
        }

        /** @todo Someone else's enum filter */
        public boolean problematic() {
            return 0 > INFO.compareTo(level);
        }

        @Override
        public String toString() {
            return line;
        }
    }
}
