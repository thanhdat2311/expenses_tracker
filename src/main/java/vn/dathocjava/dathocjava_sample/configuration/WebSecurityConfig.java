package vn.dathocjava.dathocjava_sample.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.dathocjava.dathocjava_sample.filter.JwtTokenFilter;
import vn.dathocjava.dathocjava_sample.model.Role;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests

                            .requestMatchers(HttpMethod.GET, "salemanagement/v1/company/assignedPerson/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "salemanagement/v1/tasks/company/**").permitAll()
                            //Roles
                            .requestMatchers(HttpMethod.GET, "api/v1/roles/**").hasAnyRole(Role.ADMIN)
                            // Transactions
                            .requestMatchers(HttpMethod.POST, "expenses-tracker/v1/transactions/createTransaction")
                            .hasAnyRole(Role.ADMIN,Role.USER,Role.SYSTEM_ADMIN)
                            .requestMatchers(HttpMethod.POST, "expenses-tracker/v1/chatAI")
                            .hasAnyRole(Role.ADMIN,Role.USER,Role.SYSTEM_ADMIN)
                            .requestMatchers(HttpMethod.GET, "expenses-tracker/v1/transactions/all**")
                            .hasAnyRole(Role.ADMIN,Role.USER,Role.SYSTEM_ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "expenses-tracker/v1/transactions/deleteTransaction")
                            .hasAnyRole(Role.ADMIN,Role.USER,Role.SYSTEM_ADMIN)
                            .requestMatchers(HttpMethod.PUT, "expenses-tracker/v1/transactions/**")
                            .hasAnyRole(Role.ADMIN,Role.USER,Role.SYSTEM_ADMIN)
                            // Categories
                            .requestMatchers(HttpMethod.GET, "expenses-tracker/v1/categories/**")
                            .hasAnyRole(Role.ADMIN,Role.USER,Role.SYSTEM_ADMIN)
                            .requestMatchers(HttpMethod.POST, "expenses-tracker/v1/categories/**")
                            .hasAnyRole(Role.ADMIN,Role.SYSTEM_ADMIN)
                            // status
                            .requestMatchers(HttpMethod.GET, "salemanagement/v1/status")
                            .hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(HttpMethod.POST, "salemanagement/v1/status")
                            .hasAnyRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET, "salemanagement/v1/status/**")
                            .hasAnyRole(Role.ADMIN)

                            // User
                            .requestMatchers(HttpMethod.GET, "salemanagement/v1/user/all")
                            .hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(HttpMethod.PUT, "salemanagement/v1/user/**")
                            .hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(HttpMethod.GET, "expenses-tracker/v1/user/details")
                            .hasAnyRole(Role.SYSTEM_ADMIN,Role.ADMIN,Role.USER)
                            .requestMatchers(HttpMethod.PUT, "salemanagement/v1/user/changePassword")
                            .hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(HttpMethod.POST, "salemanagement/v1/user/register")

                            .permitAll()
                            // dashboard
                            .requestMatchers(HttpMethod.POST, "salemanagement/v1/dashboard")
                            .hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(HttpMethod.GET, "salemanagement/v1/dashboard")
                            .hasAnyRole(Role.ADMIN,Role.USER)
                            // bypass Token
                            .requestMatchers(HttpMethod.POST, "expenses-tracker/v1/users/login")
                            .permitAll()
                            .requestMatchers(HttpMethod.POST, "expenses-tracker/v1/transactions/createTransaction")
                            .permitAll()
                            .requestMatchers(HttpMethod.POST, "salemanagement/v1/resetPassword/renderOtp")
                            .permitAll()
                            .requestMatchers(HttpMethod.POST, "salemanagement/v1/resetPassword/confirmOtp")
                            .permitAll()

                            .anyRequest().authenticated();
                });

        return http.build();

    }
}
