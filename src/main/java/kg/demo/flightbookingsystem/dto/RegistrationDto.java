package kg.demo.flightbookingsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDto {
    @NotBlank(message = "Email обязателен")
    @Email(message = "Введите корректный email")
    private String email;

    @NotBlank(message = "Имя обязательно")
    @Size(min = 6, max = 100, message = "Имя от 6 до 100 символов")
    private String fullName;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max = 100, message = "Пароль от 8 до 100 символов")
    private String password;

    @NotBlank(message = "Подтвердите пароль")
    private String confirmPassword;
}