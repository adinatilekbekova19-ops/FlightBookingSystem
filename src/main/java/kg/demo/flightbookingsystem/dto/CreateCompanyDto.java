package kg.demo.flightbookingsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCompanyDto {

    @NotBlank(message = "Название компании обязательно")
    private String name;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Введите корректный email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 4, message = "Пароль должен быть не менее 4 символов")
    private String password;
}