package hm.binkley;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public final class RootController {
    @Nonnull
    @RequestMapping(path = "/", method = GET)
    public Root getRoot() {
        return new Root("Hello, world!");
    }
}
