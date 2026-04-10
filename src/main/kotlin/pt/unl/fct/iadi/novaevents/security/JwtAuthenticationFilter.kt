// FILE: JwtAuthenticationFilter.kt
package pt.unl.fct.iadi.novaevents.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.cookies?.find { it.name == "jwt" }?.value

        if (token != null &&
            jwtService.isValid(token) &&
            SecurityContextHolder.getContext().authentication == null
        ) {
            try {
                val username = jwtService.extractUsername(token)
                val roles = jwtService.extractRoles(token)
                val authorities = roles.map {
                    SimpleGrantedAuthority(
                        if (it.startsWith("ROLE_")) it else "ROLE_$it"
                    )
                }

                val auth = UsernamePasswordAuthenticationToken(username, null, authorities)
                SecurityContextHolder.getContext().authentication = auth

            } catch (e: Exception) {
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }
}