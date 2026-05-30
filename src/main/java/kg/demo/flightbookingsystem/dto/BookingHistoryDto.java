package kg.demo.flightbookingsystem.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingHistoryDto {
    private Long bookingId;
    private Long ticketId;
    private String seatNumber;
    private String ticketClass;
    private BigDecimal price;
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String companyName;
    private LocalDateTime bookingDate;
    private String status;

    private String formattedDepartureTime;
    private String formattedArrivalTime;
    private String formattedBookingDate;
}