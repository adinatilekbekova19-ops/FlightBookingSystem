package kg.demo.flightbookingsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminCompanyDto {
    private Long id;
    private String name;
    private String email;
    private Boolean frozen;
    private String logoPath;
    private LocalDateTime createdAt;
    private int totalFlights;
    private List<FlightBookingInfoDto> flights;
}