package kg.demo.flightbookingsystem.service;

import kg.demo.flightbookingsystem.dto.CompanyFlightDto;
import kg.demo.flightbookingsystem.dto.CreateFlightDto;
import kg.demo.flightbookingsystem.dto.TicketDto;
import kg.demo.flightbookingsystem.entity.Company;
import kg.demo.flightbookingsystem.entity.Flight;
import kg.demo.flightbookingsystem.entity.Ticket;
import kg.demo.flightbookingsystem.entity.enums.TicketClass;
import kg.demo.flightbookingsystem.repository.CompanyRepository;
import kg.demo.flightbookingsystem.repository.FlightRepository;
import kg.demo.flightbookingsystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyFlightService {

    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final CompanyRepository companyRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Transactional
    public Flight createFlightWithTickets(Long companyId, CreateFlightDto dto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Компания не найдена"));

        String flightNumber = dto.getFlightNumber();

        if (dto.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(
                    "Дата вылета не может быть в прошлом"
            );
        }

        if (dto.getDepartureCity().equalsIgnoreCase(dto.getArrivalCity())) {
            throw new RuntimeException(
                    "Город вылета и прилета не должны совпадать"
            );
        }

        if (!dto.getArrivalTime().isAfter(dto.getDepartureTime())) {
            throw new RuntimeException(
                    "Время прилета должно быть позже времени вылета"
            );
        }

        if (Boolean.TRUE.equals(company.getFrozen())) {
            throw new RuntimeException(
                    "Компания заморожена и не может создавать рейсы"
            );
        }


        if (flightNumber == null || flightNumber.isEmpty()) {
            flightNumber = generateFlightNumber(company.getName());
        }

        Flight flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setDepartureCity(dto.getDepartureCity());
        flight.setArrivalCity(dto.getArrivalCity());
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setCompany(company);


        Flight savedFlight = flightRepository.saveAndFlush(flight);


        generateTicketsForFlight(savedFlight);

        return savedFlight;
    }

    private void generateTicketsForFlight(Flight flight) {
        String[] economySeats = {"1A", "1B", "1C", "2A", "2B", "2C", "3A"};
        BigDecimal economyPrice = calculatePrice(flight.getDepartureCity(), flight.getArrivalCity(), "ECONOMY");

        for (String seat : economySeats) {
            Ticket ticket = new Ticket();
            ticket.setSeatNumber(seat);
            ticket.setTicketClass(TicketClass.ECONOMY);
            ticket.setPrice(economyPrice);
            ticket.setIsBooked(false);
            ticket.setFlight(flight);
            ticketRepository.save(ticket);
        }

        String[] businessSeats = {"4A", "4B", "4C"};
        BigDecimal businessPrice = calculatePrice(flight.getDepartureCity(), flight.getArrivalCity(), "BUSINESS");

        for (String seat : businessSeats) {
            Ticket ticket = new Ticket();
            ticket.setSeatNumber(seat);
            ticket.setTicketClass(TicketClass.BUSINESS);
            ticket.setPrice(businessPrice);
            ticket.setIsBooked(false);
            ticket.setFlight(flight);
            ticketRepository.save(ticket);
        }
    }

    private BigDecimal calculatePrice(String departureCity, String arrivalCity, String ticketClass) {
        int basePrice = 5000;

        if (isLongDistance(departureCity, arrivalCity)) {
            basePrice = 15000;
        } else if (isMediumDistance(departureCity, arrivalCity)) {
            basePrice = 8000;
        }

        if ("BUSINESS".equals(ticketClass)) {
            basePrice = basePrice * 2;
        }

        return BigDecimal.valueOf(basePrice);
    }

    private boolean isLongDistance(String departure, String arrival) {
        return (departure.equals("Москва") && arrival.equals("Бишкек")) ||
                (departure.equals("Бишкек") && arrival.equals("Москва")) ||
                (departure.equals("Стамбул") && arrival.equals("Бишкек")) ||
                (departure.equals("Бишкек") && arrival.equals("Стамбул"));
    }

    private boolean isMediumDistance(String departure, String arrival) {
        return (departure.equals("Москва") && arrival.equals("Стамбул")) ||
                (departure.equals("Стамбул") && arrival.equals("Москва")) ||
                (departure.equals("Бишкек") && arrival.equals("Екатеринбург")) ||
                (departure.equals("Екатеринбург") && arrival.equals("Бишкек"));
    }

    private String generateFlightNumber(String companyName) {
        String prefix = switch (companyName) {
            case "Аэрофлот" -> "SU";
            case "Turkish Airlines" -> "TK";
            case "Avia Traffic" -> "AV";
            default -> "FL";
        };

        long count = flightRepository.count() + 1;
        return prefix + String.format("%04d", count);
    }

    public List<CompanyFlightDto> getCompanyFlights(Long companyId) {
        List<Flight> flights = flightRepository.findByCompanyId(companyId);

        return flights.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CompanyFlightDto convertToDto(Flight flight) {
        CompanyFlightDto dto = new CompanyFlightDto();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setDepartureCity(flight.getDepartureCity());
        dto.setArrivalCity(flight.getArrivalCity());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());

        if (flight.getDepartureTime() != null) {
            dto.setFormattedDepartureTime(flight.getDepartureTime().format(DATE_FORMATTER));
        }
        if (flight.getArrivalTime() != null) {
            dto.setFormattedArrivalTime(flight.getArrivalTime().format(DATE_FORMATTER));
        }

        List<Ticket> tickets = ticketRepository.findByFlightId(flight.getId());
        List<TicketDto> ticketDtos = tickets.stream()
                .map(this::convertTicketToDto)
                .collect(Collectors.toList());
        dto.setTickets(ticketDtos);

        long bookedCount = tickets.stream().filter(Ticket::getIsBooked).count();
        dto.setBookedTickets((int) bookedCount);
        dto.setAvailableTickets(tickets.size() - (int) bookedCount);

        return dto;
    }

    private TicketDto convertTicketToDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setSeatNumber(ticket.getSeatNumber());
        dto.setTicketClass(ticket.getTicketClass() != null ? ticket.getTicketClass().name() : "ECONOMY");
        dto.setPrice(ticket.getPrice());
        dto.setIsBooked(ticket.getIsBooked());
        return dto;
    }
}