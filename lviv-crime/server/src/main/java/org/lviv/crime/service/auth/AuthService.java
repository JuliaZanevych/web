package org.lviv.crime.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.lviv.crime.dto.auth.User;
import org.lviv.crime.dto.user.credentials.UserCredentials;
import org.lviv.crime.exception.UnauthenticatedException;
import org.lviv.crime.repository.user.UserRepository;
import org.lviv.crime.service.jwt.JwtTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static org.lviv.crime.constants.Constants.USER_CTX_KEY;

@Slf4j
@Service
public class AuthService {

    private static final String TOKEN_PREFIX = "Bearer";

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;


    public AuthService(UserRepository userRepository, JwtTokenService jwtTokenService,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<String> logIn(UserCredentials userCredentials) {
        String username = userCredentials.getUsername();

        return userRepository.findByUsername(username).map(userEntity -> {
            String password = userCredentials.getPassword();
            String passwordHash = userEntity.getPasswordHash();
            if (!passwordEncoder.matches(password, passwordHash)) {
                throw new UnauthenticatedException("Invalid password!");
            }

            User user = new User();
            user.setId(userEntity.getId());

            return jwtTokenService.generateToken(user);
        }).switchIfEmpty(Mono.error(new UnauthenticatedException("Invalid username!")));
    }

    public Context authenticateUserAndAddToContext(Context ctx, String authHeader) {
        String[] authHeaderParts = authHeader.trim().split("\\s+", 2);
        if (authHeaderParts.length != 2 || !authHeaderParts[0].equals(TOKEN_PREFIX)) {
            log.error("Invalid format of Auth token!");
            return ctx;
        }

        String tokenValue = authHeaderParts[1];

        User user;
        try {
            user = jwtTokenService.receiveUser(tokenValue);
        } catch (RuntimeException e) {
            log.error("Couldn't authenticate User!", e);
            return ctx;
        }

        return ctx.put(USER_CTX_KEY, user);
    }

}
