package hm.binkley;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(EgMain.class)
@WebIntegrationTest({"server.port:0", "management.port:0"})
public final class RootControllerIntegrationTest {
    @Test
    public void shouldRunSlow() {
        assertThat(2).isEqualTo(2);
    }
}
