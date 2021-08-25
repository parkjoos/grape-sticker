package net.jaypark.grapesticker.api.security


class OAuthAttributes(
    val attributes: MutableMap<String, Any>,
    val registrationId: String,
    val userNameAttributeName: String?,
    val id: String?,
    val name: String?,
    val email: String?

) {
    companion object {
        fun of(registrationId: String?, userNameAttributeName: String?, attribute: Map<String, Any>): OAuthAttributes {
            return when (registrationId) {
                "kakao" -> ofKakao(registrationId, userNameAttributeName, attribute)
                else -> throw RuntimeException("not defined oAuth2 registration id")
            }
        }

        private fun ofKakao(
            registrationId: String,
            userNameAttributeName: String?,
            attribute: Map<String, Any>
        ): OAuthAttributes {
            val id = attribute[userNameAttributeName]
            val kakaoAccount: Map<String, Any> = attribute["kakao_account"] as Map<String, Any>
            val profile = mutableMapOf<String, Any>()
            profile.putAll(kakaoAccount["profile"] as Map<String, Any>)
            profile["nickname"]?.also { profile["username"] = it }
            kakaoAccount["email"]?.also { profile["email"] = it }
            profile["id"] = id.toString()

            return OAuthAttributes(
                registrationId = registrationId,
                userNameAttributeName = userNameAttributeName,
                attributes = profile,
                id = id.toString(),
                name = profile["username"] as String?,
                email = profile["email"] as String?
            )
        }
    }
}