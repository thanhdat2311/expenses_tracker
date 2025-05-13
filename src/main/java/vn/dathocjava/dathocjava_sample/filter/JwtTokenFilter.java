package vn.dathocjava.dathocjava_sample.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import vn.dathocjava.dathocjava_sample.components.JwtTokenUtil;
import vn.dathocjava.dathocjava_sample.model.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isByPassToken(request)) {
                System.out.println(">>> Đã vào filter trước khi gọi controller: " + request.getRequestURI());

                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (!authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7);
                final String email = jwtTokenUtil.extractEmail(token);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = (User) userDetailsService.loadUserByUsername(email);
                    if (jwtTokenUtil.validateToken(token, user)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (
                Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isByPassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of("salemanagement/v1/resetPassword/renderOtp", "POST"),
                Pair.of("salemanagement/v1/resetPassword/confirmOtp", "POST"),
                Pair.of("expenses-tracker/v1/users/login", "POST"),
                Pair.of("salemanagement/v1/user/register", "POST")

        );
        for (Pair<String, String> byPassToken : byPassTokens) {
            if (request.getServletPath().contains(byPassToken.getLeft())
                    && request.getMethod().equals(byPassToken.getRight())) {
                return true;
            }
        }
        return false;
    }
}
