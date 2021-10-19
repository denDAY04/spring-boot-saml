package dk.asj.springsaml.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class HomeController {

    @GetMapping
    public Mono<String> index() {
        return Mono.just("hello from unrestricted endpoint");
    }

    @GetMapping("/secret")
    @Secured({ "ROLE_USER" })
    public Mono<String> secret() {
      return Mono.just("hello from a secret endpoint");
    }
}
