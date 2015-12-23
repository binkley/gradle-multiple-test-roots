package hm.binkley;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.DisallowWriteToSystemErr;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
public final class RootControllerTest {
    private static final Pattern NELINE = compile("\n");

    @Rule
    public final SystemOutRule sout = new SystemOutRule().
            enableLog().
            muteForSuccessfulTests();
    @Rule
    public final DisallowWriteToSystemErr noErr
            = new DisallowWriteToSystemErr();

    private MockMvc mvc;

    @Before
    public void setUp()
            throws Exception {
        mvc = standaloneSetup(new RootController()).build();
    }

    @After
    public void noProblematicLogLines() {
        final String platformIndependentLogLines = sout
                .getLogWithNormalizedLineSeparator();
        assertThat(NELINE.splitAsStream(platformIndependentLogLines).
                map(LogLine::new).
                filter(LogLine::problematic).
                collect(toList())).
                isEmpty();
    }

    @Test
    public void shouldGetRoot()
            throws Exception {
        mvc.perform(get("/").
                accept(APPLICATION_JSON_UTF8)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.message", equalTo("Hello, world!")));
    }
}
