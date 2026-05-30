package kg.demo.flightbookingsystem.service;

import kg.demo.flightbookingsystem.dto.RegistrationDto;
import kg.demo.flightbookingsystem.entity.User;
import kg.demo.flightbookingsystem.entity.enums.Role;
import kg.demo.flightbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(RegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        return userRepository.save(user);
    }
}