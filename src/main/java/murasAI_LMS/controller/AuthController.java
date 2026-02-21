package murasAI_LMS.controller;

import java.util.Optional; // важно!

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import murasAI_LMS.dto.AuthRequest;
import murasAI_LMS.model.User;
import murasAI_LMS.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Логин
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request, HttpSession session) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return "User not found";
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Wrong password";
        }

        session.setAttribute("userId", user.getId());

        return "Login success";
    }

    @GetMapping("/me")
    public Object me(HttpSession session) {

        Object userId = session.getAttribute("userId");

        if (userId == null) {
            return "Not logged in";
        }

        Optional<User> optionalUser = userRepository.findById((Long) userId);
        return optionalUser.orElse(null); // если нет — вернется null
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logged out";
    }

    @PostMapping("/create-test-user")
    public String createTestUser() {

        String hashedPassword = passwordEncoder.encode("azixjs08");

        User user = new User();
        user.setEmail("azixjs08@gmail.com");
        user.setPassword(hashedPassword);

        userRepository.save(user);

        return "Test user created";
    }
}