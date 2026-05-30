package kg.demo.flightbookingsystem.controller;

import kg.demo.flightbookingsystem.dto.RegistrationDto;
import kg.demo.flightbookingsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegistrationDto dto,
                           BindingResult result,
                           Model model) {
        log.info("Попытка регистрации: {}", dto.getEmail());
        Map<String, String> errors = new HashMap<>();

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            log.warn("Ошибка: пароли не совпадают для {}", dto.getEmail());
            errors.put("confirmPassword", "Пароли не совпадают");
        }

        if (userService.existsByEmail(dto.getEmail())) {
            log.warn("Ошибка: email уже зарегистрирован {}", dto.getEmail());
            errors.put("email", "Этот email уже зарегистрирован");
        }

        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }

        if (!errors.isEmpty()) {
            log.warn("Регистрация отклонена: {} ошибок для {}", errors.size(), dto.getEmail());
            model.addAttribute("fields", errors);
            model.addAttribute("registrationDto", dto);
            return "register";
        }

        userService.registerUser(dto);
        log.info("Успешная регистрация: {}", dto.getEmail());
        return "redirect:/login?registered";
    }
}