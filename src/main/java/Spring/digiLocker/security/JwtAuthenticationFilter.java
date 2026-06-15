package Spring.digiLocker.security;

import Spring.digiLocker.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JWTService jwtService;

    public JwtAuthenticationFilter(
            JWTService jwtService
    ) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader =
                request.getHeader(
                        "Authorization"
                );

        if(
                authHeader == null ||
                        !authHeader.startsWith("Bearer ")
        ) {
            filterChain.doFilter(
                    request,
                    response
            );
            return;
        };
        String jwt =
                authHeader.substring(7);
        if(!jwtService.isTokenValid(jwt)) {
            filterChain.doFilter(
                    request,
                    response
            );
            return;
        }
        Long userId =
                jwtService.extractUserId(jwt);

        String role =
                jwtService.extractRole(jwt);
    }
}