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

        if (token != null && jwtService.isValid(token)) {
            try {
                val username = jwtService.extractUsername(token)
                val roles = jwtService.extractRoles(token)
                val authorities = roles.map { SimpleGrantedAuthority(it) }

                val auth = UsernamePasswordAuthenticationToken(username, null, authorities)

                // Coloca a autenticação no contexto
                SecurityContextHolder.getContext().authentication = auth

            } catch (e: Exception) {
                // Token inválido ou expirado → limpar contexto
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }
}