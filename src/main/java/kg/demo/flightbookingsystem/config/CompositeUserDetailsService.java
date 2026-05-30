package kg.demo.flightbookingsystem.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CompositeUserDetailsService implements UserDetailsService {

    private final UserDetailsService first;
    private final UserDetailsService second;

    public CompositeUserDetailsService(UserDetailsService first, UserDetailsService second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return first.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return second.loadUserByUsername(username);
        }
    }
}