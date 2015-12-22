package hm.binkley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootApplication
public class EgMain {
    public static void main(final String... args) {
        SpringApplication.run(EgMain.class, args);
    }

    public void fast() {}

    public void slow()
            throws InterruptedException {
        SECONDS.sleep(1L);
    }
}
