package kpn.server.api.authentication

import kpn.api.common.common.UserSession
import kpn.database.base.SessionDatabase
import kpn.server.api.Api
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.not
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(api: Api, sessionDatabase: SessionDatabase) {

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

  @GetMapping(Array("/oauth2/users"))
  def users(@AuthenticationPrincipal principal: OAuth2User): Seq[UserSession] = {
    if (principal.getName == "vmarc") {
      val pipeline = Seq(
        filter(
          not(
            equal("principal", null),
          )
        )
      )
      sessionDatabase.sessions.aggregate[UserSession](pipeline)
    }
    else {
      Seq.empty
    }
  }
}
