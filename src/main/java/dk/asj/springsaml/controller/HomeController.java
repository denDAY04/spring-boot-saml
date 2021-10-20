package dk.asj.springsaml.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HomeController {

    @GetMapping
    public String index() {
        return "hello from unrestricted endpoint";
    }

    @GetMapping("/secret")
    @Secured({ "ROLE_USER" })
    public String secret() {
      return "hello from a secret endpoint";
    }
}
