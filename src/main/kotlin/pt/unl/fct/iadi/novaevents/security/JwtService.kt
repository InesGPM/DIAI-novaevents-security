package pt.unl.fct.iadi.novaevents.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    private val secret: SecretKey = Keys.hmacShaKeyFor(
        "super-secret-key-super-secret-key-123456".toByteArray()
    )

    private val expirationMs = 1000 * 60 * 60 * 24

    fun generateToken(username: String, roles: List<String>): String {
        val now = Date()

        return Jwts.builder()
            .subject(username)
            .claim("roles", roles)
            .issuedAt(now)
            .expiration(Date(now.time + expirationMs))
            .signWith(secret)
            .compact()
    }

    fun extractUsername(token: String): String {
        return Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun extractRoles(token: String): List<String> {
        val claims = Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .payload

        @Suppress("UNCHECKED_CAST")
        return claims["roles"] as List<String>
    }

    fun isValid(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}