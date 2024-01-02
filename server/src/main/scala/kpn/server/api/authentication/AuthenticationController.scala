package kpn.server.api.authentication

import kpn.api.common.common.UserSession
import kpn.database.base.SessionDatabase
import kpn.server.api.Api
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import scala.jdk.CollectionConverters.CollectionHasAsScala

@RestController
class AuthenticationController(api: Api, sessionDatabase: SessionDatabase, sessionRegistry: SessionRegistry) {

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

    val allSessionInformations = sessionRegistry.getAllPrincipals().asScala.flatMap { principal =>
      sessionRegistry.getAllSessions(principal, false).asScala
    }.toSeq


    api.execute("users") {
      val pipeline = Seq(
        //          filter(
        //            not(
        //              equal("principal", null),
        //            )
        //          )
      )
      val databaseSessions = sessionDatabase.sessions.aggregate[UserSession](pipeline)
      val memorySessions = allSessionInformations.map { session =>
        UserSession(
          _id = session.getSessionId,
          intervalSeconds = 0,
          createdMillis = 0,
          accessedMillis = 0,
          expireAt = "",
          principal = Some(session.getPrincipal.toString)
        )
      }
      databaseSessions ++ memorySessions
    }
  }
}
