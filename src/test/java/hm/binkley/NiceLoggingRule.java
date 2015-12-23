package hm.binkley;

import hm.binkley.LogLine.LogLineFactory;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.regex.Pattern;

import static hm.binkley.LogLine.LogLineFactory.logLineFactory;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.RuleChain.outerRule;

public final class NiceLoggingRule<E extends Enum<E>>
        implements TestRule {
    private static final Pattern NEWLINE = compile("\n");

    private final SystemOutRule sout = new SystemOutRule().
            enableLog().
            muteForSuccessfulTests();
    private final SystemErrRule serr = new SystemErrRule().
            enableLog();

    private final RuleChain delegate;

    @Nonnull
    public static <E extends Enum<E>> NiceLoggingRule<E> niceLoggingRule(
            @Nonnull final String logLineFormat,
            @NotNull final Collection<E> logLevels) {
        return new NiceLoggingRule<E>(logLineFormat, logLevels);
    }

    @Nonnull
    @SafeVarargs
    public static <E extends Enum<E>> NiceLoggingRule<E> niceLoggingRule(
            @Nonnull final String logLineFormat,
            @NotNull final E... logLevels) {
        return niceLoggingRule(logLineFormat, asList(logLevels));
    }

    private NiceLoggingRule(final String logLineFormat,
            final Collection<E> logLevels) {
        delegate = outerRule(new CheckLogging(logLineFormat, logLevels)).
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
        private final LogLineFactory lines;

        public CheckLogging(final String logLineFormat,
                final Collection<E> logLevels) {
            lines = logLineFactory(format(logLineFormat, logLevels.stream().
                    map(Enum::name).
                    collect(joining("|"))));
        }

        @Override
        protected void after() {
            final String cleanSerr = serr.getLogWithNormalizedLineSeparator();
            assertThat(NEWLINE.splitAsStream(cleanSerr).
                    collect(toList())).
                    isEmpty();

            final String cleanSout = sout.getLogWithNormalizedLineSeparator();
            assertThat(NEWLINE.splitAsStream(cleanSout).
                    map(lines::newLogLine).
                    filter(LogLine::problematic).
                    collect(toList())).
                    isEmpty();
        }
    }
}
