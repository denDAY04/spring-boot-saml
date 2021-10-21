package dk.asj.springsaml.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping
    public String index() {
        return "hello from unrestricted endpoint";
    }

    @GetMapping("/secret")
    public String secret(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal) {
      principal.getAttributes().forEach((a, values) ->
        log.info("Principle has attribute {} : [{}]", a, values.stream().map(Object::toString).collect(Collectors.joining(", ")))
      );
      return "hello from a secret endpoint";
    }
}
