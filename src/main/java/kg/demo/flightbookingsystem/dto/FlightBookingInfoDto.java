package kg.demo.flightbookingsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FlightBookingInfoDto {
    private Long id;
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int bookingsCount;
}