package net.jaypark.grapesticker.api.security

import net.jaypark.grapesticker.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class GrapeStickerOAuth2User(
    private val member: Member,
    private val oAuthAttributes: OAuthAttributes?,
    private val authorities: MutableSet<out GrantedAuthority>
) : OAuth2User {
    fun getMemberId(): String {
        return member.id!!
    }

    override fun getName(): String {
        return oAuthAttributes?.name!!
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return oAuthAttributes?.attributes!!
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }
}
