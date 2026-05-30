package kg.demo.flightbookingsystem.controller;

import kg.demo.flightbookingsystem.dto.RegistrationDto;
import kg.demo.flightbookingsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        // Проверка совпадения паролей
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Пароли не совпадают");
        }

        // Проверка уникальности email
        if (userService.existsByEmail(dto.getEmail())) {
            result.rejectValue("email", "error.email", "Email уже зарегистрирован");
        }

        if (result.hasErrors()) {
            return "register";
        }

        userService.registerUser(dto);
        return "redirect:/login?registered";
    }
}