package kg.demo.flightbookingsystem.service;

import kg.demo.flightbookingsystem.dto.AdminCompanyDto;
import kg.demo.flightbookingsystem.dto.CreateCompanyDto;
import kg.demo.flightbookingsystem.dto.FlightBookingInfoDto;
import kg.demo.flightbookingsystem.entity.Company;
import kg.demo.flightbookingsystem.entity.Flight;
import kg.demo.flightbookingsystem.repository.BookingRepository;
import kg.demo.flightbookingsystem.repository.CompanyRepository;
import kg.demo.flightbookingsystem.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CompanyRepository companyRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Company createCompany(CreateCompanyDto dto) {
        Company company = new Company();
        company.setName(dto.getName());
        company.setEmail(dto.getEmail());
        company.setPassword(passwordEncoder.encode(dto.getPassword()));
        company.setFrozen(false);
        company.setCreatedAt(LocalDateTime.now());
        return companyRepository.save(company);
    }

    public boolean isEmailExists(String email) {
        return companyRepository.existsByEmail(email);
    }

    public List<AdminCompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean freezeCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Компания не найдена"));

        boolean hasActiveBookings = bookingRepository.existsActiveBookingsByCompanyId(companyId);

        if (hasActiveBookings) {
            return false;
        }

        company.setFrozen(true);
        companyRepository.save(company);
        return true;
    }

    @Transactional
    public void unfreezeCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Компания не найдена"));
        company.setFrozen(false);
        companyRepository.save(company);
    }

    private AdminCompanyDto toDto(Company company) {
        AdminCompanyDto dto = new AdminCompanyDto();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setEmail(company.getEmail());
        dto.setFrozen(company.getFrozen());
        dto.setCreatedAt(company.getCreatedAt());

        List<Flight> flights = flightRepository.findByCompanyId(company.getId());
        dto.setTotalFlights(flights.size());

        dto.setFlights(flights.stream()
                .map(flight -> {
                    FlightBookingInfoDto flightDto = new FlightBookingInfoDto();
                    flightDto.setId(flight.getId());
                    flightDto.setFlightNumber(flight.getFlightNumber());
                    flightDto.setDepartureCity(flight.getDepartureCity());
                    flightDto.setArrivalCity(flight.getArrivalCity());
                    flightDto.setDepartureTime(flight.getDepartureTime());
                    flightDto.setArrivalTime(flight.getArrivalTime());
                    flightDto.setBookingsCount(bookingRepository.countByFlightId(flight.getId()));
                    return flightDto;
                })
                .collect(Collectors.toList()));

        return dto;
    }
}