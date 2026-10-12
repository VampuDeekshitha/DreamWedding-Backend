package com.tietheknot.service;

import com.tietheknot.entity.User;
import com.tietheknot.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        String email = user.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered.");
        }

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("HOST");
        }

        return userRepository.save(user);
    }

    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmail(email.trim().toLowerCase(Locale.ROOT))
                .filter(user -> {
                    String savedPassword = user.getPassword();
                    boolean passwordMatches = isBcryptHash(savedPassword)
                            ? passwordEncoder.matches(password, savedPassword)
                            : savedPassword.equals(password);

                    // Upgrade accounts created before password hashing was introduced.
                    if (passwordMatches && !isBcryptHash(savedPassword)) {
                        user.setPassword(passwordEncoder.encode(password));
                        userRepository.save(user);
                    }
                    return passwordMatches;
                });
    }

    private boolean isBcryptHash(String password) {
        return password != null && (password.startsWith("$2a$")
                || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }

}
