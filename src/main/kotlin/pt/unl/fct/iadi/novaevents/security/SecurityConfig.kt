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
import pt.unl.fct.iadi.novaevents.security.RedirectCookieAuthenticationEntryPoint
import org.springframework.http.HttpMethod
@Configuration
@EnableMethodSecurity
open class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtLoginSuccessHandler: JwtLoginSuccessHandler,
    private val redirectCookieAuthenticationEntryPoint: RedirectCookieAuthenticationEntryPoint
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
            .exceptionHandling {
                it.authenticationEntryPoint(redirectCookieAuthenticationEntryPoint)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/", "/login", "/login/**").permitAll()
                    .requestMatchers("/clubs", "/clubs/*").permitAll()
                    .requestMatchers("/events", "/events/*").permitAll()

                    // detalhe de evento é público
                    .requestMatchers(HttpMethod.GET, "/clubs/*/events/*").permitAll()

                    // criar/editar/apagar exige autenticação
                    .requestMatchers(HttpMethod.GET, "/clubs/*/events/new").authenticated()
                    .requestMatchers(HttpMethod.POST, "/clubs/*/events").authenticated()
                    .requestMatchers(HttpMethod.GET, "/clubs/*/events/*/edit").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/clubs/*/events/*").authenticated()
                    .requestMatchers(HttpMethod.POST, "/clubs/*/events/*").authenticated()
                    .requestMatchers(HttpMethod.GET, "/clubs/*/events/*/delete").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/clubs/*/events/*").authenticated()

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