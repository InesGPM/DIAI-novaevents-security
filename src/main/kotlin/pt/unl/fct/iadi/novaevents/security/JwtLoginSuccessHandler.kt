// FILE: JwtLoginSuccessHandler.kt
package pt.unl.fct.iadi.novaevents.security

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class JwtLoginSuccessHandler(
    private val jwtService: JwtService
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val username = authentication.name
        val roles = authentication.authorities.map { it.authority }

        val token = jwtService.generateToken(username, roles)

        val cookie = Cookie("jwt", token).apply {
            isHttpOnly = true
            path = "/"
            // secure = true     // descomenta em produção com HTTPS
            maxAge = 60 * 60 * 24   // 24 horas (mesmo tempo do JWT)
        }

        response.addCookie(cookie)

        // Redirect explícito - resolve muitos problemas de loop
        response.status = HttpServletResponse.SC_FOUND   // 302
        response.setHeader("Location", "/events")
    }
}