package Spring.digiLocker.security;

import Spring.digiLocker.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

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
        System.out.println("JWT FILTER HIT");
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
        System.out.println("1");
        if(!jwtService.isTokenValid(jwt)) {
            filterChain.doFilter(
                    request,
                    response
            );
            return;
        }
        System.out.println("2");
        Long userId =
                jwtService.extractUserId(jwt);

        System.out.println("3");
        String role =
                jwtService.extractRole(jwt);
        System.out.println("4");

        List<SimpleGrantedAuthority> authorities =
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        )
                );
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        authorities
                );
        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        authentication
                );
        System.out.println("AUTHENTICATION SET");
        filterChain.doFilter(
                request,
                response
        );
    }
}