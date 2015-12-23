package hm.binkley;

import org.springframework.boot.logging.LogLevel;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static org.springframework.boot.logging.LogLevel.INFO;

/** @todo General logback parser based on logging pattern */
public final class LogLine {
    private static final String levels = Stream.of(LogLevel.values()).
            map(LogLevel::name).
            collect(joining("|"));
    private static final Pattern logLinePattern = compile(
            "^(?<timestamp>\\d{4,4}-\\d{2,2}-\\d{2,2} \\d{2,2}:\\d{2,2}:\\d{2,2}\\.\\d{3,3}) +(?<level>"
                    + levels + ") +");
    @Nonnull
    private final String line;
    @Nonnull
    private final LogLevel level;

    public LogLine(@Nonnull final String line) {
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
