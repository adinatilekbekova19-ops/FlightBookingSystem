package kg.demo.flightbookingsystem.controller;

import kg.demo.flightbookingsystem.dto.CreateCompanyDto;
import kg.demo.flightbookingsystem.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("companies", adminService.getAllCompanies());
        model.addAttribute("createCompanyDto", new CreateCompanyDto());
        return "admin/dashboard";
    }

    @PostMapping("/companies/create")
    public String createCompany(@Valid @ModelAttribute CreateCompanyDto dto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (adminService.isEmailExists(dto.getEmail())) {
            result.rejectValue("email", "error.email", "Компания с таким email уже существует");
        }

        if (result.hasErrors()) {
            model.addAttribute("companies", adminService.getAllCompanies());
            return "admin/dashboard";
        }

        adminService.createCompany(dto);
        redirectAttributes.addFlashAttribute("message", "Компания успешно создана");
        return "redirect:/admin";
    }

    @PostMapping("/companies/{id}/freeze")
    public String freezeCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean success = adminService.freezeCompany(id);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Компания заморожена");
        } else {
            redirectAttributes.addFlashAttribute("error", "Нельзя заморозить компанию с активными бронированиями");
        }
        return "redirect:/admin";
    }

    @PostMapping("/companies/{id}/unfreeze")
    public String unfreezeCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.unfreezeCompany(id);
        redirectAttributes.addFlashAttribute("message", "Компания разморожена");
        return "redirect:/admin";
    }
}