package kg.demo.flightbookingsystem.service;

import kg.demo.flightbookingsystem.entity.Booking;
import kg.demo.flightbookingsystem.entity.Ticket;
import kg.demo.flightbookingsystem.entity.User;
import kg.demo.flightbookingsystem.entity.enums.BookingStatus;
import kg.demo.flightbookingsystem.repository.BookingRepository;
import kg.demo.flightbookingsystem.repository.TicketRepository;
import kg.demo.flightbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Transactional
    public Booking bookTicket(Long userId, Long ticketId, String selectedSeat) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Билет не найден"));

        Ticket ticketToBook;

        if (selectedSeat != null && !selectedSeat.isBlank()) {

            ticketToBook = ticketRepository
                    .findByFlightIdAndSeatNumber(
                            ticket.getFlight().getId(),
                            selectedSeat
                    )
                    .orElseThrow(() ->
                            new RuntimeException("Место не найдено"));

        } else {
            ticketToBook = ticket;
        }

        if (ticketToBook.getIsBooked()) {
            throw new RuntimeException(
                    "Место уже занято"
            );
        }

        ticketToBook.setIsBooked(true);
        ticketRepository.save(ticketToBook);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTicket(ticketToBook);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.ACTIVE);

        return bookingRepository.save(booking);
    }



}