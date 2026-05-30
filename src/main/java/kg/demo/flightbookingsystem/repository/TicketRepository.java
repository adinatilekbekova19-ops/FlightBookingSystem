package kg.demo.flightbookingsystem.repository;

import kg.demo.flightbookingsystem.entity.Ticket;
import kg.demo.flightbookingsystem.entity.enums.TicketClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByFlightIdAndIsBookedFalse(Long flightId);

    List<Ticket> findByFlightId(Long flightId);


    Optional<Ticket> findByFlightIdAndSeatNumber(Long flightId, String seatNumber);

    List<Ticket> findByFlightIdAndTicketClassAndIsBookedFalse(Long flightId, TicketClass ticketClass);


    @Query("SELECT t FROM Ticket t " +
            "JOIN FETCH t.flight f " +
            "WHERE f.id IN :flightIds AND t.isBooked = false")
    List<Ticket> findByFlightIdInAndIsBookedFalse(@Param("flightIds") List<Long> flightIds);


}