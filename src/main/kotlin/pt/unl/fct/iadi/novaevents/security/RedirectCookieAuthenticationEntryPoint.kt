package pt.unl.fct.iadi.novaevents.security

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class RedirectCookieAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val originalUrl = request.requestURI +
                (request.queryString?.let { "?$it" } ?: "")

        val cookie = Cookie("REDIRECT_URI", originalUrl).apply {
            path = "/"
            isHttpOnly = false
        }

        response.addCookie(cookie)
        response.sendRedirect("/login")
    }
}