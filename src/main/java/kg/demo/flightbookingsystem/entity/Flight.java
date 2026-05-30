package kg.demo.flightbookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false, unique = true)
    private String flightNumber;

    @Column(name = "departure_city", nullable = false)
    private String departureCity;

    @Column(name = "arrival_city", nullable = false)
    private String arrivalCity;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    public String getCompanyName() {
        return company != null ? company.getName() : "";
    }

    public String getCompanyLogo() {
        return company != null && company.getLogoPath() != null ? company.getLogoPath() : "/images/default-logo.png";
    }

    public String getFormattedDepartureTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return departureTime != null ? departureTime.format(formatter) : "";
    }

    public String getFormattedArrivalTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return arrivalTime != null ? arrivalTime.format(formatter) : "";
    }
}
