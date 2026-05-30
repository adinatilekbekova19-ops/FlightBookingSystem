package kg.demo.flightbookingsystem.service;

import kg.demo.flightbookingsystem.dto.FlightDto;
import kg.demo.flightbookingsystem.dto.FlightSearchDto;
import kg.demo.flightbookingsystem.dto.TicketDto;
import kg.demo.flightbookingsystem.entity.Flight;
import kg.demo.flightbookingsystem.entity.Ticket;
import kg.demo.flightbookingsystem.repository.FlightRepository;
import kg.demo.flightbookingsystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Page<FlightDto> searchFlights(FlightSearchDto searchDto, int page, int size) {
        log.info("Поиск рейсов: город вылета={}, город прилета={}",
                searchDto.getDepartureCity(), searchDto.getArrivalCity());
        Pageable pageable = PageRequest.of(page, size);

        LocalDateTime startDateTime = Optional.ofNullable(searchDto.getStartDate())
                .map(LocalDate::atStartOfDay)
                .orElse(null);

        LocalDateTime endDateTime = Optional.ofNullable(searchDto.getEndDate())
                .map(date -> date.atTime(LocalTime.MAX))
                .orElse(null);

        String departureCity = (searchDto.getDepartureCity() != null && !searchDto.getDepartureCity().isEmpty())
                ? searchDto.getDepartureCity() : null;
        String arrivalCity = (searchDto.getArrivalCity() != null && !searchDto.getArrivalCity().isEmpty())
                ? searchDto.getArrivalCity() : null;

        Page<Flight> flightPage = flightRepository.searchFlightsWithCompany(departureCity, arrivalCity, startDateTime, endDateTime, pageable);

        if (flightPage.getContent().isEmpty()) {
            log.warn("Рейсы не найдены");
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<Long> flightIds = flightPage.getContent().stream()
                .map(Flight::getId)
                .collect(Collectors.toList());

        List<Ticket> allTickets = ticketRepository.findByFlightIdInAndIsBookedFalse(flightIds);

        Map<Long, List<Ticket>> ticketsByFlight = allTickets.stream()
                .collect(Collectors.groupingBy(ticket -> ticket.getFlight().getId()));

        List<FlightDto> flightDtos = flightPage.getContent().stream()
                .map(flight -> convertToDto(flight, ticketsByFlight.getOrDefault(flight.getId(), List.of())))
                .collect(Collectors.toList());

        return new PageImpl<>(flightDtos, pageable, flightPage.getTotalElements());
    }




    public List<String> getAllDepartureCities() {
        return flightRepository.findAllDepartureCities();
    }

    public List<String> getAllArrivalCities() {
        return flightRepository.findAllArrivalCities();
    }

    private FlightDto convertToDto(Flight flight, List<Ticket> tickets) {
        FlightDto dto = new FlightDto();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setDepartureCity(flight.getDepartureCity());
        dto.setArrivalCity(flight.getArrivalCity());

        if (flight.getDepartureTime() != null) {
            dto.setDepartureTimeFormatted(flight.getDepartureTime().format(DATE_FORMATTER));
        }
        if (flight.getArrivalTime() != null) {
            dto.setArrivalTimeFormatted(flight.getArrivalTime().format(DATE_FORMATTER));
        }

        if (flight.getCompany() != null) {
            dto.setCompanyName(flight.getCompany().getName());
        }

        List<TicketDto> ticketDtos = tickets.stream()
                .map(this::convertTicketToDto)
                .collect(Collectors.toList());
        dto.setTickets(ticketDtos);

        return dto;
    }

    private TicketDto convertTicketToDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setSeatNumber(ticket.getSeatNumber());
        dto.setTicketClass(ticket.getTicketClass() != null ? ticket.getTicketClass().name() : "ECONOMY");
        dto.setPrice(ticket.getPrice());
        dto.setIsBooked(ticket.getIsBooked());
        dto.setFlightId(ticket.getFlight() != null ? ticket.getFlight().getId() : null);
        return dto;
    }
}