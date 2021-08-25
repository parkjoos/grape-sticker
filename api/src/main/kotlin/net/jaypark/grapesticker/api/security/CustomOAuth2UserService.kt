package net.jaypark.grapesticker.api.security

import net.jaypark.grapesticker.domain.Member
import net.jaypark.grapesticker.service.MemberService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class CustomOAuth2UserService(
    val memberService: MemberService
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val delegate = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)

        val registrationId = userRequest?.clientRegistration?.registrationId
        val userNameAttributeName =
            userRequest?.clientRegistration?.providerDetails?.userInfoEndpoint?.userNameAttributeName

        val oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.attributes)

        val member = memberService.joinOrUpdate(
            Member(
                oAuth2Id = oAuthAttributes.id,
                name = oAuthAttributes.name,
                email = oAuthAttributes.email
            )
        )
        return GrapeStickerOAuth2User(
            member, oAuthAttributes,
            oAuth2User.authorities as MutableSet<out GrantedAuthority>
        )
    }
}