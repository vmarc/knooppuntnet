package kpn.backend

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class UserService extends OAuth2UserService[OAuth2UserRequest, OAuth2User]{

  override def loadUser(userRequest: OAuth2UserRequest): OAuth2User = {
    val accessToken = userRequest.getAccessToken
    // TODO use accessToken to pick up the username from OpenStreetMap (use UserParser)
    new KpnUser("vmarc")
  }
}
