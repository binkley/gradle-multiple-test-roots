package hm.binkley;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.JsonPathExpectationsHelper;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.text.ParseException;

import static hm.binkley.NiceLoggingRule.niceLoggingRule;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(EgMain.class)
@WebIntegrationTest({"server.port:0", "management.port:0"})
public final class RootControllerIntegrationTest {
    @Rule
    public final NiceLoggingRule<LogLevel> niceLogging = niceLoggingRule(
            "^(?<timestamp>\\d{4,4}-\\d{2,2}-\\d{2,2} \\d{2,2}:\\d{2,2}:\\d{2,2}\\.\\d{3,3}) +(?<level>%s) +",
            LogLevel.values());

    private final RestOperations template = new TestRestTemplate();

    @Value("${local.server.port}")
    private int port;

    /** @todo Clumsy use of internal Spring test class */
    @Test
    public void shouldGetRoot()
            throws ParseException {
        final String root = template.getForObject(uriFor("/"), String.class);
        new JsonPathExpectationsHelper("$.message").
                assertValue(root, equalTo("Hello, world!"));
    }

    private URI uriFor(final String path) {
        return URI.create(format("http://localhost:%d/%s", port, path));
    }
}
