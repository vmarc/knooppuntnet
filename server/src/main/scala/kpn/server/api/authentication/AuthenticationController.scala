package kpn.server.api.authentication

import kpn.server.api.Api
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(api: Api) {

  private val log = LoggerFactory.getLogger(classOf[AuthenticationController])

  @GetMapping(Array("/oauth2/user"))
  def user(@AuthenticationPrincipal principal: OAuth2User): String = {
    api.execute("user") {
      if (principal != null) {
        principal.getAttribute("name")
      }
      else {
        null
      }
    }
  }
}
