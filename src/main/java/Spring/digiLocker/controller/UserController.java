package Spring.digiLocker.controller;

import Spring.digiLocker.dto.LoginRequest;
import Spring.digiLocker.dto.LoginResponse;
import Spring.digiLocker.dto.RegisterRequest;
import Spring.digiLocker.entity.User;
import Spring.digiLocker.services.JWTService;
import Spring.digiLocker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;
    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/users")
    public User createUser(
            @Valid @RequestBody RegisterRequest request) {
        System.out.println("LOGIN ENDPOINT HIT");
        return userService.createUser(request);
    }
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
    @GetMapping("/test")
    public String test(
            @RequestParam String token
    ) {
        return jwtService.extractEmail(token);
    }
}
