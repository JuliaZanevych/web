package org.lviv.crime.aspect.auth;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.lviv.crime.exception.UnauthenticatedException;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static org.lviv.crime.constants.Constants.USER_CTX_KEY;

@Aspect
@Configuration
@AllArgsConstructor
public class AuthAspect {

    private static final UnauthenticatedException UNAUTHENTICATED_EXCEPTION =
            new UnauthenticatedException("The user is not unauthenticated!");

    @Around("@annotation(org.lviv.crime.auth.Auth)" +
            " && within(@org.springframework.web.bind.annotation.RestController *)")
    public Object checkAuthoritiesSpecifiedByMethodAnnotation(ProceedingJoinPoint pjp) throws Throwable {
        return verifyUserAccessAndProceed(pjp);
    }

    @Around("@within(org.lviv.crime.auth.Auth)" +
            " && !@annotation(org.lviv.crime.auth.Auth)" +
            " && within(@org.springframework.web.bind.annotation.RestController *)")
    public Object checkAuthoritiesSpecifiedByTypeAnnotation(ProceedingJoinPoint pjp) throws Throwable {
        return verifyUserAccessAndProceed(pjp);
    }

    private Object verifyUserAccessAndProceed(ProceedingJoinPoint pjp) throws Throwable {
        Mono<Context> verifyAccessMono = Mono.subscriberContext().doOnNext(ctx -> {
            if (!ctx.hasKey(USER_CTX_KEY)) {
                throw UNAUTHENTICATED_EXCEPTION;
            }
        });

        Object responseObj = pjp.proceed();

        if (responseObj instanceof Mono<?>) {
            return verifyAccessMono.then((Mono<?>) responseObj);
        }

        if (responseObj instanceof Flux<?>) {
            return verifyAccessMono.thenMany((Flux<?>) responseObj);
        }

        return responseObj;
    }
}
