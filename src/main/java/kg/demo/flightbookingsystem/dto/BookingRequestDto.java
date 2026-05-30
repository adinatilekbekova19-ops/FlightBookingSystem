package kg.demo.flightbookingsystem.dto;

import lombok.Data;

@Data
public class BookingRequestDto {
    private Long ticketId;
    private String seatNumber;
}