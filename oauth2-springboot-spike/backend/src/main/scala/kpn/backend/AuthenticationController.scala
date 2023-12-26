package kpn.backend

import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController {
  private val log = LoggerFactory.getLogger(classOf[AuthenticationController])

  @GetMapping(Array("/user")) def user(@AuthenticationPrincipal principal: OAuth2User): String = {
    principal.getAttribute("name")
  }

  @GetMapping(value = Array("/api/page1"))
  def page1(): String = {
    log.info("GET page1()")
    "public contents from server, that does not require to be logged in"
  }

  @GetMapping(value = Array("/api/page2"))
  def page2(@AuthenticationPrincipal principal: OAuth2User): String = {
    log.info("GET page2()")
    if (principal != null && principal.getName != "anonymousUser") {
      "contents from server that is shown when logged in"
    }
    else {
      "contents from server that is shown when NOT logged in"
    }
  }
}
