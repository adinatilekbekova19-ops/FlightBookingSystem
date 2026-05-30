package kg.demo.flightbookingsystem.service;

import kg.demo.flightbookingsystem.dto.RegistrationDto;
import kg.demo.flightbookingsystem.entity.User;
import kg.demo.flightbookingsystem.entity.enums.Role;
import kg.demo.flightbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional
    public User registerUser(RegistrationDto dto) {
        log.debug("Создание пользователя: {}", dto.getEmail());

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);

        User saved = userRepository.save(user);
        log.info("Пользователь сохранён: ID={}, email={}", saved.getId(), saved.getEmail());

        return saved;
    }
}