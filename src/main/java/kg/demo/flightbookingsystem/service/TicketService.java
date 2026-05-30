package kg.demo.flightbookingsystem.service;

import kg.demo.flightbookingsystem.dto.TicketDto;
import kg.demo.flightbookingsystem.entity.Ticket;
import kg.demo.flightbookingsystem.entity.enums.TicketClass;
import kg.demo.flightbookingsystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;



    public TicketDto getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket == null) return null;

        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setSeatNumber(ticket.getSeatNumber());
        dto.setTicketClass(ticket.getTicketClass() != null ? ticket.getTicketClass().name() : "ECONOMY");
        dto.setPrice(ticket.getPrice());
        dto.setIsBooked(ticket.getIsBooked());

        if (ticket.getFlight() != null) {
            dto.setFlightId(ticket.getFlight().getId());
            dto.setDepartureCity(ticket.getFlight().getDepartureCity());
            dto.setArrivalCity(ticket.getFlight().getArrivalCity());
            dto.setFlightNumber(ticket.getFlight().getFlightNumber());
            if (ticket.getFlight().getCompany() != null) {
                dto.setCompanyName(ticket.getFlight().getCompany().getName());
            }
        }

        return dto;
    }

    public List<String> getAvailableSeatsByFlightIdAndClass(
            Long flightId,
            String ticketClass
    ) {

        List<Ticket> tickets =
                ticketRepository
                        .findByFlightIdAndTicketClassAndIsBookedFalse(
                                flightId,
                                TicketClass.valueOf(ticketClass)
                        );

        return tickets.stream()
                .map(Ticket::getSeatNumber)
                .collect(Collectors.toList());
    }
}