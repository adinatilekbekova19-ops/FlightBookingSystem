package kg.demo.flightbookingsystem.controller;

import jakarta.validation.Valid;
import kg.demo.flightbookingsystem.dto.CompanyFlightDto;
import kg.demo.flightbookingsystem.dto.CreateFlightDto;
import kg.demo.flightbookingsystem.security.CustomUserDetails;
import kg.demo.flightbookingsystem.service.CompanyFlightService;
import kg.demo.flightbookingsystem.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyFlightService companyFlightService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null || !"ROLE_COMPANY".equals(userDetails.getRole())) {
            return "redirect:/login";
        }

        Long companyId = userDetails.getId();
        var company = companyService.findById(companyId);
        List<CompanyFlightDto> flights = companyFlightService.getCompanyFlights(companyId);

        model.addAttribute("company", company);
        model.addAttribute("flights", flights);

        return "company/dashboard";
    }

    @GetMapping("/flights/create")
    public String showCreateFlightForm(Model model) {
        model.addAttribute("createFlightDto", new CreateFlightDto());
        return "company/create-flight";
    }

    @PostMapping("/flights/create")
    public String createFlight(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @Valid @ModelAttribute CreateFlightDto dto,
                               RedirectAttributes redirectAttributes) {
        try {
            companyFlightService.createFlightWithTickets(userDetails.getId(), dto);
            redirectAttributes.addFlashAttribute("message", "Рейс успешно создан");
        }  catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute(
                "error",
                "Ошибка при создании рейса: " + e.getMessage()
        );
    }
        return "redirect:/company/dashboard";
    }

    @PostMapping("/logo/upload")
    public String uploadLogo(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestParam("logo") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        try {
            String logoPath = companyService.uploadLogo(file);
            companyService.updateLogo(userDetails.getId(), logoPath);
            redirectAttributes.addFlashAttribute("message", "Логотип успешно загружен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке логотипа");
        }
        return "redirect:/company/dashboard";
    }
}