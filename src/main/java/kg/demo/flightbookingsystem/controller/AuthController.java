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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String registered,
                            @RequestParam(required = false) String accessDenied,
                            @RequestParam(required = false) String locked,
                            @RequestParam(required = false) String logout,
                            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Неверный логин/email или пароль");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Регистрация успешна! Теперь вы можете войти в систему.");
        }
        if (accessDenied != null) {
            model.addAttribute("errorMessage", "У вас нет прав для доступа к этой странице");
        }
        if (locked != null) {
            model.addAttribute("errorMessage", "Ваша компания заморожена. Обратитесь к администратору.");
        }
        if (logout != null) {
            model.addAttribute("infoMessage", "Вы успешно вышли из системы.");
        }

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
            errors.put("confirmPassword", "Пароли не совпадают");
        }

        if (userService.existsByEmail(dto.getEmail())) {
            errors.put("email", "Этот email уже зарегистрирован");
        }

        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("fields", errors);  // ← переменная fields
            model.addAttribute("registrationDto", dto);
            return "register";
        }

        userService.registerUser(dto);
        return "redirect:/login?registered";
    }
}