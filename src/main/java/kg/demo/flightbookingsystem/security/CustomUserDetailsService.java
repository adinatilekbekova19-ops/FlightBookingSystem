package kg.demo.flightbookingsystem.security;

import kg.demo.flightbookingsystem.entity.Company;
import kg.demo.flightbookingsystem.entity.User;
import kg.demo.flightbookingsystem.repository.CompanyRepository;
import kg.demo.flightbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Поиск пользователя: {}", email);
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new CustomUserDetails(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole().name(),
                    true
            );
        }

        Company company = companyRepository.findByEmail(email);
        if (company != null) {
            return new CustomUserDetails(
                    company.getId(),
                    company.getEmail(),
                    company.getPassword(),
                    "ROLE_COMPANY",
                    !company.getFrozen()
            );
        }

        log.warn("Пользователь не найден: {}", email);
        throw new UsernameNotFoundException("Пользователь не найден: " + email);
    }
}