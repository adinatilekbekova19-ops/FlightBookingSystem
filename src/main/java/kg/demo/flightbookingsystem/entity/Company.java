package kg.demo.flightbookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "logo_path")
    private String logoPath;               // Путь к загруженному логотипу

    @Column(nullable = false)
    private Boolean frozen = false;        // заморожена ли компания (не может создавать рейсы)

    @Column(name = "created_at")
    private LocalDateTime createdAt;       // дата создания компании

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flight> flights = new ArrayList<>();  // список рейсов компании

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
