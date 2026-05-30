package kg.demo.flightbookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import kg.demo.flightbookingsystem.entity.enums.TicketClass;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketClass ticketClass;       // класс ECONOMY или BUSINESS

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;              // стоимость билета

    @Column(nullable = false)
    private Boolean isBooked = false;      // Забронирован ли билет true = занят

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;                 // Рейс, к которому относится билет

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Booking booking;               // Бронирование (если билет занят)
}