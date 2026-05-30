package kg.demo.flightbookingsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompanyFlightDto {
    private Long id;
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String formattedDepartureTime;
    private String formattedArrivalTime;
    private List<TicketDto> tickets;
    private int availableTickets;
    private int bookedTickets;
}