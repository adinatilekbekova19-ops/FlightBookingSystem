package kg.demo.flightbookingsystem.repository;

import kg.demo.flightbookingsystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.ticket t " +
            "JOIN FETCH t.flight f " +
            "JOIN FETCH f.company c " +
            "WHERE b.user.id = :userId " +
            "ORDER BY b.bookingDate DESC")
    List<Booking> findBookingsWithDetailsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "JOIN b.ticket t " +
            "JOIN t.flight f " +
            "JOIN f.company c " +
            "WHERE c.id = :companyId AND b.status = 'ACTIVE'")
    boolean existsActiveBookingsByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(b) FROM Booking b " +
            "JOIN b.ticket t " +
            "WHERE t.flight.id = :flightId")
    int countByFlightId(@Param("flightId") Long flightId);
}