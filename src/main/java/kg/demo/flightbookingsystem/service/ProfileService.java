package kg.demo.flightbookingsystem.service;

import kg.demo.flightbookingsystem.dto.BookingHistoryDto;
import kg.demo.flightbookingsystem.entity.Booking;
import kg.demo.flightbookingsystem.entity.User;
import kg.demo.flightbookingsystem.repository.BookingRepository;
import kg.demo.flightbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String getFormattedCreatedAt(User user) {
        if (user != null && user.getCreatedAt() != null) {
            return user.getCreatedAt().format(DATE_FORMATTER);
        }
        return "";
    }

    public List<BookingHistoryDto> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findBookingsWithDetailsByUserId(userId);

        return bookings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BookingHistoryDto convertToDto(Booking booking) {
        BookingHistoryDto dto = new BookingHistoryDto();

        dto.setBookingId(booking.getId());
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(booking.getStatus().name());

        if (booking.getBookingDate() != null) {
            dto.setFormattedBookingDate(booking.getBookingDate().format(DATE_FORMATTER));
        }

        if (booking.getTicket() != null) {
            dto.setTicketId(booking.getTicket().getId());
            dto.setSeatNumber(booking.getTicket().getSeatNumber());
            dto.setPrice(booking.getTicket().getPrice());

            if (booking.getTicket().getTicketClass() != null) {
                dto.setTicketClass(booking.getTicket().getTicketClass().name());
            }

            if (booking.getTicket().getFlight() != null) {
                var flight = booking.getTicket().getFlight();
                dto.setFlightNumber(flight.getFlightNumber());
                dto.setDepartureCity(flight.getDepartureCity());
                dto.setArrivalCity(flight.getArrivalCity());
                dto.setDepartureTime(flight.getDepartureTime());
                dto.setArrivalTime(flight.getArrivalTime());

                if (flight.getDepartureTime() != null) {
                    dto.setFormattedDepartureTime(flight.getDepartureTime().format(DATE_FORMATTER));
                }
                if (flight.getArrivalTime() != null) {
                    dto.setFormattedArrivalTime(flight.getArrivalTime().format(DATE_FORMATTER));
                }

                if (flight.getCompany() != null) {
                    dto.setCompanyName(flight.getCompany().getName());
                }
            }
        }

        return dto;
    }
}