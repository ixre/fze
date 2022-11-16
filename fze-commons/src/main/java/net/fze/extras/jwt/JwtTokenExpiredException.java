package net.fze.extras.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;

import java.time.Instant;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(String message, Instant expiredOn) {
        super(message);
    }

    public JwtTokenExpiredException(TokenExpiredException ex) {
        super(ex.getMessage(),null);
    }
}
