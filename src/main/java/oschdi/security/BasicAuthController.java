package oschdi.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Base64;

@CrossOrigin()
@RestController
@RequestMapping("")
public class BasicAuthController {

    @PostMapping("/login")
    public Principal login(Principal principal) {
        return principal;
    }
}