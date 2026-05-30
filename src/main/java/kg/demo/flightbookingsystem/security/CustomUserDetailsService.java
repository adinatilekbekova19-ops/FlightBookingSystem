package kg.demo.flightbookingsystem.security;

import kg.demo.flightbookingsystem.entity.Company;
import kg.demo.flightbookingsystem.entity.User;
import kg.demo.flightbookingsystem.repository.CompanyRepository;
import kg.demo.flightbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ищем среди пользователей
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new CustomUserDetails(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole().name(),
                    true  // Пользователи не блокируются
            );
        }

        // Ищем среди компаний
        Company company = companyRepository.findByEmail(email);
        if (company != null) {
            return new CustomUserDetails(
                    company.getId(),
                    company.getEmail(),
                    company.getPassword(),
                    "ROLE_COMPANY",
                    !company.getFrozen()  // frozen = true → блокировка
            );
        }

        throw new UsernameNotFoundException("Пользователь не найден: " + email);
    }
}