package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.entity.Admin;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class AuthorizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    public AuthorizationService(PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
    }

    public String login(ApplicationUser applicationUser, String password) {
        LOGGER.info("Login user with email {}", applicationUser.getEmail());

        UserDetails userDetails = loadUser(applicationUser);

        LOGGER.debug("Password {}, encoded password {}", password, userDetails.getPassword());
        if (userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
            && userDetails.isEnabled()
            && passwordEncoder.matches(password, userDetails.getPassword())) {
            LOGGER.info("Login successful");


            List<String> grantedAuthorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

            return jwtTokenizer.getAuthToken(userDetails.getUsername(), grantedAuthorities);
        } else {
            LOGGER.info("Invalid login attempt");

            throw new BadCredentialsException("Username or password is incorrect or account is locked");
        }
    }

    public Long getSessionUserId() {

        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private UserDetails loadUser(ApplicationUser applicationUser) {

        List<GrantedAuthority> grantedAuthorities;
        if (applicationUser instanceof Admin) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
        } else if (applicationUser instanceof Doctor) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_DOCTOR", "ROLE_USER");
        } else if (applicationUser instanceof Researcher) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_RESEARCHER", "ROLE_USER");
        } else {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        }
        return new User(applicationUser.getId().toString(), applicationUser.getPassword(), applicationUser.getStatus().equals(Status.ACTIVE), true, true, true, grantedAuthorities);
    }
}
