package kg.demo.flightbookingsystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class FlightDto {
    private Long id;
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private String departureTimeFormatted;
    private String arrivalTimeFormatted;
    private String companyName;
    private List<TicketDto> tickets;
}