package kg.demo.flightbookingsystem.controller;

import kg.demo.flightbookingsystem.dto.BookingRequestDto;
import kg.demo.flightbookingsystem.dto.FlightDto;
import kg.demo.flightbookingsystem.dto.FlightSearchDto;
import kg.demo.flightbookingsystem.dto.TicketDto;
import kg.demo.flightbookingsystem.security.CustomUserDetails;
import kg.demo.flightbookingsystem.service.BookingService;
import kg.demo.flightbookingsystem.service.FlightService;
import kg.demo.flightbookingsystem.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;
    private final TicketService ticketService;
    private final BookingService bookingService;

    @GetMapping
    public String searchFlights(@ModelAttribute("searchDto") FlightSearchDto searchDto,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(required = false) String message,
                                @RequestParam(required = false) String error,
                                Model model) {
        Page<FlightDto> flightPage = flightService.searchFlights(searchDto, page, 10);

        model.addAttribute("flights", flightPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", flightPage.getTotalPages());
        model.addAttribute("searchDto", searchDto);
        model.addAttribute("departureCities", flightService.getAllDepartureCities());
        model.addAttribute("arrivalCities", flightService.getAllArrivalCities());

        if (message != null) {
            model.addAttribute("message", message);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "flights";
    }

    @GetMapping("/book/{ticketId}")
    public String showBookingForm(@PathVariable Long ticketId,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String role = userDetails.getRole();
        if ("ROLE_ADMIN".equals(role) || "ROLE_COMPANY".equals(role)) {
            return "redirect:/flights?error=Доступ запрещён";
        }

        TicketDto ticket = ticketService.getTicketById(ticketId);
        if (ticket == null || ticket.getIsBooked()) {
            return "redirect:/flights?error=Билет уже забронирован";
        }

        var availableSeats = ticketService.getAvailableSeatsByFlightIdAndClass(ticket.getFlightId(), ticket.getTicketClass());
        model.addAttribute("ticket", ticket);
        model.addAttribute("availableSeats", availableSeats);
        model.addAttribute("bookingRequest", new BookingRequestDto());

        return "booking-modal";
    }

    @PostMapping("/book/confirm")
    public String confirmBooking(@ModelAttribute BookingRequestDto bookingRequest,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            bookingService.bookTicket(userDetails.getId(), bookingRequest.getTicketId(), bookingRequest.getSeatNumber());
            redirectAttributes.addAttribute("message", "Билет успешно забронирован");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/flights";
    }
}