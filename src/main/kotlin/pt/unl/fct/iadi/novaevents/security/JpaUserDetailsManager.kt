package pt.unl.fct.iadi.novaevents.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.*
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.model.User
import pt.unl.fct.iadi.novaevents.repository.UserRepository

@Service
class JpaUserDetailsManager(
    private val userRepository: UserRepository
) : UserDetailsManager {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found: $username") }

        return toUserDetails(user)
    }

    private fun toUserDetails(user: User): UserDetails {
        val authorities: Collection<GrantedAuthority> =
            user.roles.map { SimpleGrantedAuthority(it.name.name) }

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            authorities
        )
    }

    override fun userExists(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    override fun createUser(user: UserDetails) {
        throw UnsupportedOperationException("Not needed")
    }

    override fun updateUser(user: UserDetails) {
        throw UnsupportedOperationException("Not needed")
    }

    override fun deleteUser(username: String) {
        throw UnsupportedOperationException("Not needed")
    }

    override fun changePassword(oldPassword: String?, newPassword: String?) {
        throw UnsupportedOperationException("Not needed")
    }
}