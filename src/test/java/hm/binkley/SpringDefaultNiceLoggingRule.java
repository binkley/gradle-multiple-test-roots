package hm.binkley;

import org.springframework.boot.logging.LogLevel;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.springframework.boot.logging.LogLevel.INFO;
import static org.springframework.boot.logging.LogLevel.OFF;

public final class SpringDefaultNiceLoggingRule {
    private static final String logLevels = Stream.of(LogLevel.values()).
            filter(level -> OFF != level).
            map(Enum::name).
            collect(joining("|"));

    public static NiceLoggingRule springDefaultNiceLoggingRule() {
        return new NiceLoggingRule(
                "^(?<timestamp>\\d{4,4}-\\d{2,2}-\\d{2,2} \\d{2,2}:\\d{2,2}:\\d{2,2}\\.\\d{3,3}) +(?<level>"
                        + logLevels + ") +",
                SpringDefaultNiceLoggingRule::problematic);
    }

    private static boolean problematic(final String level) {
        return 0 > INFO.compareTo(LogLevel.valueOf(level));
    }
}
