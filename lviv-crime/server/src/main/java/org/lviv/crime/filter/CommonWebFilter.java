package org.lviv.crime.filter;

import lombok.AllArgsConstructor;
import org.lviv.crime.dto.auth.User;
import org.lviv.crime.service.auth.AuthService;
import org.lviv.crime.service.jwt.JwtTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;

import static org.lviv.crime.constants.Constants.AUTH_HEADER;
import static org.lviv.crime.constants.Constants.USER_CTX_KEY;

@Component
@AllArgsConstructor
public class CommonWebFilter implements WebFilter {

    private final AuthService authService;

    private final JwtTokenService jwtTokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        HttpHeaders responseHeaders = response.getHeaders();

        addCorsHeaders(responseHeaders);

        response.beforeCommit(() -> addRefreshedAuthTokenToResponse(responseHeaders));

        return webFilterChain.filter(serverWebExchange)
                .subscriberContext(ctx -> authenticateUserAndAddToContext(serverWebExchange, ctx));
    }

    private Context authenticateUserAndAddToContext(ServerWebExchange serverWebExchange, Context ctx) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        List<String> authHeaderValues = request.getHeaders().get(AUTH_HEADER);
        if (authHeaderValues == null || authHeaderValues.size() != 1) {
            return ctx;
        }

        return authService.authenticateUserAndAddToContext(ctx, authHeaderValues.get(0));
    }

    private Mono<Void> addRefreshedAuthTokenToResponse(HttpHeaders responseHeaders) {
        return Mono.subscriberContext().doOnNext(ctx -> {
            if (!ctx.hasKey(USER_CTX_KEY)) {
                return;
            }

            User user = ctx.get(USER_CTX_KEY);
            responseHeaders.add("Access-Control-Expose-Headers", AUTH_HEADER);
            responseHeaders.add(AUTH_HEADER, jwtTokenService.generateToken(user));
        }).then();
    }

    private void addCorsHeaders(HttpHeaders responseHeaders) {
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        responseHeaders.add("Access-Control-Allow-Headers", "origin,content-type,accept," +
                "x-requested-with," + AUTH_HEADER);
        responseHeaders.add("Access-Control-Allow-Credentials", "true");
        responseHeaders.add("Access-Control-Allow-Methods", "GET,POST,PUT,PATCH,DELETE,OPTIONS,HEAD");
        responseHeaders.add("Access-Control-Max-Age", "1209600");
    }
}
