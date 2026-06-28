package Spring.digiLocker.services;

import Spring.digiLocker.dto.LoginRequest;
import Spring.digiLocker.dto.LoginResponse;
import Spring.digiLocker.dto.RegisterRequest;
import Spring.digiLocker.entity.User;
import Spring.digiLocker.exception.InvalidOperationException;
import Spring.digiLocker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JWTService jwtService;

    public UserService(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User createUser(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            System.out.println("EMAIL ALREADY EXISTS");
            throw new InvalidOperationException("Email already exists");
        }
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        Optional<User> userOptional =
                userRepository.findByEmail(
                        request.getEmail()
                );

        if(userOptional.isEmpty()) {
            throw new InvalidOperationException(
                    "Invalid Credentials"
            );
        }
        User user = userOptional.get();

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidOperationException(
                "Invalid Credentials"
        );
        }

        String token =
                jwtService.generateToken(
                        user.getId(),
                        user.getEmail(),
                        user.getRole().name()
                );

        return new LoginResponse(token);
    }
    }


