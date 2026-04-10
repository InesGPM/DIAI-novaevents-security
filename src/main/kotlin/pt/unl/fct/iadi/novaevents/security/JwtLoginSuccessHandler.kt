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

        val redirectUri = request.cookies
            ?.find { it.name == "REDIRECT_URI" }
            ?.value ?: "/events"

        val clearRedirectCookie = Cookie("REDIRECT_URI", "").apply {
            path = "/"
            maxAge = 0
        }
        response.addCookie(clearRedirectCookie)

        val finalRedirect = if (redirectUri.startsWith("http://") || redirectUri.startsWith("https://")) {
            redirectUri
        } else {
            val isDefaultPort =
                (request.scheme == "http" && request.serverPort == 80) ||
                        (request.scheme == "https" && request.serverPort == 443)

            val portPart = if (isDefaultPort) "" else ":${request.serverPort}"

            "${request.scheme}://${request.serverName}$portPart${request.contextPath}$redirectUri"
        }

        response.status = HttpServletResponse.SC_FOUND
        response.setHeader("Location", finalRedirect)
    }
}