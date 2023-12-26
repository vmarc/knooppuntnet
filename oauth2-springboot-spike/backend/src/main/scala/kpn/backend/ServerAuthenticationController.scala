package kpn.backend

import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ServerAuthenticationController {
  private val log = LoggerFactory.getLogger(classOf[ServerAuthenticationController])

  @GetMapping(Array("/oauth2/user"))
  def user(@AuthenticationPrincipal principal: OAuth2User): String = {
    if (principal != null) {
      principal.getAttribute("name")
    }
    else {
      null
    }
  }

  @GetMapping(value = Array("/api/page1"))
  def page1(): String = {
    log.info("GET page1()")
    "public contents from server, that does not require to be logged in"
  }

  @GetMapping(value = Array("/api/page2"))
  def page2(): String = {

    val principal = SecurityContextHolder.getContext.getAuthentication.getPrincipal
    val name = principal match {
      case user: ServerAuthenticationUser => Some(user.getName)
      case _ => None
    }

    log.info(s"GET page2() name=$name")
    if (name.isDefined) {
      "contents from server that is shown when logged in"
    }
    else {
      "contents from server that is shown when NOT logged in"
    }
  }
}
