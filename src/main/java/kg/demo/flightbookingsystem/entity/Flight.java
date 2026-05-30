package kg.demo.flightbookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
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
    private String flightNumber;           // уникальный номер рейса генерируется автоматически

    @Column(name = "departure_city", nullable = false)
    private String departureCity;          // город вылета

    @Column(name = "arrival_city", nullable = false)
    private String arrivalCity;            // Город прилёта

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;   // Дв вылета

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;     // Дв прилёта

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;               // Компания владелец рейса

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();  // список билетов на этот рейс (всего 10)

}
