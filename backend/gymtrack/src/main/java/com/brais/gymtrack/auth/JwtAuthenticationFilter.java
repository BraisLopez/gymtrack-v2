package com.brais.gymtrack.auth;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.brais.gymtrack.user.User;
import com.brais.gymtrack.user.UserRepository;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Reads the JWT token from the Authorization header and authenticates the user.
 *
 * Expected header:
 * Authorization: Bearer <token>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
        ) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Removes "Bearer " from token

        //Now fixed so it protects corrupted/invalid tokens
        try{
            String email = jwtService.extractEmail(token);

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                User user = userRepository.findByEmail(email).orElse(null);

                if(user != null && jwtService.isTokenValid(token, user)){
                    var authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                    );
                    var authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }

            filterChain.doFilter(request, response);

        }catch(JwtException ex){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String body = """
            {
                "timestamp": "%s",
                "status": 401,
                "error": "Unauthorized",
                "message": "Invalid or expired token",
                "path": "%s"
            }
            """.formatted(
                LocalDateTime.now().toString(),
                request.getRequestURI()
            );
            response.getWriter().write(body);
        }
    }

}
