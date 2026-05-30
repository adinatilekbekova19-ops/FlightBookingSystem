package kg.demo.flightbookingsystem.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TicketDto {
    private Long id;
    private Long flightId;
    private String seatNumber;
    private String ticketClass;
    private BigDecimal price;
    private Boolean isBooked;

    private String departureCity;
    private String arrivalCity;
    private String flightNumber;
    private String companyName;
}