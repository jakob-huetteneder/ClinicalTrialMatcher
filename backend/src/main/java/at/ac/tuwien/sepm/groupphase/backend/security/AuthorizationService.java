package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.entity.Admin;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
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

/**
 * Service for authorization
 */
@Service
public class AuthorizationService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    public AuthorizationService(PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
    }

    /**
     * Login user
     *
     * @param applicationUser user to login
     * @param password        password of user
     * @return jwt token
     */
    public String login(ApplicationUser applicationUser, String password) {
        LOG.trace("login({}, {})", applicationUser, password);
        LOG.debug("Login user with email {}", applicationUser.getEmail());

        UserDetails userDetails = loadUser(applicationUser);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            LOG.debug("Invalid login attempt {}", applicationUser.getEmail());

            throw new BadCredentialsException("Password is incorrect.");
        }
        if (!userDetails.isEnabled()) {
            LOG.debug("Invalid login attempt {}", applicationUser.getEmail());

            throw new BadCredentialsException("Account is disabled.");
        }
        if (userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
            && userDetails.isEnabled()
            && passwordEncoder.matches(password, userDetails.getPassword())) {
            LOG.debug("Login successful {}", applicationUser.getEmail());


            List<String> grantedAuthorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

            return jwtTokenizer.getAuthToken(userDetails.getUsername(), grantedAuthorities);
        } else {
            LOG.debug("Invalid login attempt {}", applicationUser.getEmail());

            throw new BadCredentialsException("Invalid login attempt.");
        }
    }

    /**
     * Get session user id
     *
     * @return session user id
     */
    public Long getSessionUserId() {
        LOG.trace("getSessionUserId()");
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Load user details from applicationUser
     *
     * @param applicationUser user to load
     * @return user details
     */
    private UserDetails loadUser(ApplicationUser applicationUser) {
        LOG.trace("loadUser({})", applicationUser);
        List<GrantedAuthority> grantedAuthorities;
        if (applicationUser instanceof Admin) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
        } else if (applicationUser instanceof Doctor) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_DOCTOR", "ROLE_USER");
        } else if (applicationUser instanceof Researcher) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_RESEARCHER", "ROLE_USER");
        } else {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_PATIENT", "ROLE_USER");
        }
        return new User(applicationUser.getId().toString(), applicationUser.getPassword(), applicationUser.getStatus().equals(ApplicationUser.Status.ACTIVE), true, true, true, grantedAuthorities);
    }
}
