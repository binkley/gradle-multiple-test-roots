package hm.binkley;

import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.fail;
import static org.junit.rules.RuleChain.outerRule;

public final class NiceLoggingRule
        implements TestRule {
    private static final Pattern NEWLINE = compile("\n");

    private final SystemOutRule sout = new SystemOutRule().
            enableLog().
            muteForSuccessfulTests();
    private final SystemErrRule serr = new SystemErrRule().
            enableLog();

    private final Pattern logLinePattern;
    private final Predicate<String> problematic;
    private final RuleChain delegate;

    public NiceLoggingRule(final String logLinePattern,
            final Predicate<String> problematic) {
        this.logLinePattern = compile(logLinePattern);
        this.problematic = problematic;
        delegate = outerRule(NiceLoggingStatement::new).
                around(sout).
                around(serr);
    }

    @Override
    public Statement apply(final Statement base,
            final Description description) {
        return delegate.apply(base, description);
    }

    private final class NiceLoggingStatement
            extends Statement {
        private final Statement base;
        private final Description description;

        private NiceLoggingStatement(final Statement base,
                final Description description) {
            this.base = base;
            this.description = description;
        }

        @Override
        public void evaluate()
                throws Throwable {
            base.evaluate();
            checkSystemErr(description);
            checkSystemOut(description);
        }

        private void checkSystemErr(final Description description) {
            final String cleanSerr = serr.getLogWithNormalizedLineSeparator();
            final List<String> errors = NEWLINE.splitAsStream(cleanSerr).
                    collect(toList());
            if (!errors.isEmpty())
                fail("Output to System.err from " + description + ":\n"
                        + cleanSerr);
        }

        private void checkSystemOut(final Description description) {
            final String cleanSout = sout.getLogWithNormalizedLineSeparator();
            final List<LogLine> problems = NEWLINE.splitAsStream(cleanSout).
                    map(LogLine::new).
                    filter(LogLine::problematic).
                    collect(toList());
            if (!problems.isEmpty())
                fail(problems.stream().
                        map(Object::toString).
                        collect(joining("",
                                "Problems to System.out from " + description
                                        + ":\n", "")));
        }
    }

    private final class LogLine {
        @Nonnull
        private final String line;
        @Nonnull
        private final String level;

        private LogLine(@Nonnull final String line) {
            final Matcher match = logLinePattern.matcher(line);
            if (!match.find()) // Not match! Ignore trailing CR?NL
                fail(format(
                        "Log line does not match expected pattern (%s): %s",
                        logLinePattern.pattern(), line));
            this.line = line;
            level = match.group("level");
        }

        public boolean problematic() {
            return problematic.test(level);
        }

        @Override
        public String toString() {
            return line;
        }
    }
}
