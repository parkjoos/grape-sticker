package net.jaypark.grapesticker.api.security

import net.jaypark.grapesticker.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class GrapeStickerOAuth2User(private val member: Member, val oAuthAttributes: OAuthAttributes?) : OAuth2User {
    override fun getName(): String {
        return member.name ?: ""
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return oAuthAttributes?.attributes ?: mutableMapOf()
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableSetOf(GrantedAuthority { "ROLE_USER" })
    }

}
