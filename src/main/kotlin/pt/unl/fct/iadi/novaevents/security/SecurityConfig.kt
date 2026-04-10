// FILE: SecurityConfig.kt
package pt.unl.fct.iadi.novaevents.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import pt.unl.fct.iadi.novaevents.security.JwtAuthenticationFilter
import pt.unl.fct.iadi.novaevents.security.JwtLoginSuccessHandler

@Configuration
@EnableMethodSecurity
open class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtLoginSuccessHandler: JwtLoginSuccessHandler
) {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf {
                it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/", "/login", "/login/**").permitAll()   // <- importante
                    .requestMatchers("/clubs", "/clubs/*").permitAll()
                    .requestMatchers("/events", "/events/*").permitAll()
                    .requestMatchers("/clubs/*/events/new").hasAnyRole("EDITOR", "ADMIN")
                    .requestMatchers("/clubs/*/events/*/edit").hasAnyRole("EDITOR", "ADMIN")
                    .requestMatchers("/clubs/*/events/*/delete").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .formLogin {
                it
                    .loginPage("/login")
                    .successHandler(jwtLoginSuccessHandler)
                    .permitAll()
            }
            .logout {
                it
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .deleteCookies("jwt")
                    .permitAll()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}