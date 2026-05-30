package kg.demo.flightbookingsystem.controller;

import kg.demo.flightbookingsystem.dto.BookingHistoryDto;
import kg.demo.flightbookingsystem.security.CustomUserDetails;
import kg.demo.flightbookingsystem.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        var user = profileService.getUserById(userDetails.getId());
        String formattedCreatedAt = profileService.getFormattedCreatedAt(user);

        List<BookingHistoryDto> bookings = profileService.getUserBookings(userDetails.getId());

        model.addAttribute("user", user);
        model.addAttribute("formattedCreatedAt", formattedCreatedAt);
        model.addAttribute("bookings", bookings);
        model.addAttribute("bookingsCount", bookings.size());

        return "profile";
    }
}