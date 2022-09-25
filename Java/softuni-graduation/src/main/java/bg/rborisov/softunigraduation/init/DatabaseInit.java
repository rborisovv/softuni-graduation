package bg.rborisov.softunigraduation.init;

import bg.rborisov.softunigraduation.dao.AuthorityRepository;
import bg.rborisov.softunigraduation.dao.RoleRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.model.Authority;
import bg.rborisov.softunigraduation.model.Role;
import bg.rborisov.softunigraduation.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.RoleAuthority.USER_AUTHORITIES;
import static bg.rborisov.softunigraduation.enumeration.RoleEnum.OWNER;
import static bg.rborisov.softunigraduation.enumeration.RoleEnum.USER;

@Component
public class DatabaseInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInit(UserRepository userRepository, RoleRepository roleRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0 && roleRepository.count() == 0 && authorityRepository.count() == 0) {

            Set<Authority> authorities = this.mapAuthorities();

            Role OwnerRole = Role.builder()
                    .name(OWNER.name())
                    .authorities(authorities)
                    .build();

            Role userRole = Role.builder()
                    .name(USER.name())
                    .authorities(authorities)
                    .build();


            User user = User.builder()
                    .userId(RandomStringUtils.randomAscii(10).replaceAll("\s", ""))
                    .username("radi2000")
                    .password(passwordEncoder.encode("Borisov00!"))
                    .email("radii2000@abv.bg")
                    .firstName("Radoslav")
                    .lastName("Borisov")
                    .role(OwnerRole)
                    .joinDate(LocalDate.now())
                    .birthDate(LocalDate.now())
                    .imageUrl(null)
                    .isLocked(false)
                    .build();


            authorityRepository.saveAll(authorities);
            roleRepository.save(OwnerRole);
            roleRepository.save(userRole);
            userRepository.save(user);
        }
    }

    private Set<Authority> mapAuthorities() {
        return USER_AUTHORITIES
                .stream()
                .map(Authority::new)
                .collect(Collectors.toSet());
    }
}