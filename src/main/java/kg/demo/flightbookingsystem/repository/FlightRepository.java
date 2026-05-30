package kg.demo.flightbookingsystem.repository;

import kg.demo.flightbookingsystem.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByCompanyId(Long companyId);

    @Query("SELECT f FROM Flight f WHERE " +
            "(:departureCity IS NULL OR f.departureCity LIKE %:departureCity%) AND " +
            "(:arrivalCity IS NULL OR f.arrivalCity LIKE %:arrivalCity%) AND " +
            "(:startDate IS NULL OR f.departureTime >= :startDate) AND " +
            "(:endDate IS NULL OR f.departureTime <= :endDate)")
    Page<Flight> searchFlights(@Param("departureCity") String departureCity,
                               @Param("arrivalCity") String arrivalCity,
                               @Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate,
                               Pageable pageable);

    @Query("SELECT DISTINCT f FROM Flight f " +
            "LEFT JOIN FETCH f.company " +
            "WHERE (:departureCity IS NULL OR f.departureCity LIKE %:departureCity%) AND " +
            "(:arrivalCity IS NULL OR f.arrivalCity LIKE %:arrivalCity%) AND " +
            "(:startDate IS NULL OR f.departureTime >= :startDate) AND " +
            "(:endDate IS NULL OR f.departureTime <= :endDate)")
    Page<Flight> searchFlightsWithCompany(@Param("departureCity") String departureCity,
                                          @Param("arrivalCity") String arrivalCity,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate,
                                          Pageable pageable);

    @Query("SELECT DISTINCT f.departureCity FROM Flight f ORDER BY f.departureCity")
    List<String> findAllDepartureCities();

    @Query("SELECT DISTINCT f.arrivalCity FROM Flight f ORDER BY f.arrivalCity")
    List<String> findAllArrivalCities();

    List<Flight> findByCompanyIdIn(List<Long> companyIds);


}