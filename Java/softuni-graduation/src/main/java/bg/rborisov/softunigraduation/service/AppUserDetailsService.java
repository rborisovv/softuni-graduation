package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.model.Authority;
import bg.rborisov.softunigraduation.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.USERNAME_OR_PASSWORD_INCORRECT;

public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USERNAME_OR_PASSWORD_INCORRECT));
        return this.userDetails(user);
    }

    private UserDetails userDetails(User user) {
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().getName())
                .authorities(this.mapAuthorities(user.getRole().getAuthorities()))
                .accountLocked(user.getIsLocked())
                .build();
    }

    private Set<SimpleGrantedAuthority> mapAuthorities(Set<Authority> authorities) {
        return authorities.stream()
                .map(this::map)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private String map(Authority authority) {
        return authority.getName();
    }
}