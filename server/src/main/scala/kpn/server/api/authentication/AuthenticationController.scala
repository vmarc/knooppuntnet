package kpn.server.api.authentication

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import kpn.server.api.Api
import kpn.server.config.AuthenticationConfiguration
import kpn.server.config.RequestContext
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(api: Api) {

  @GetMapping(Array("/api/oauth2/user"))
  def user(): String = {
    api.execute("user") {
      RequestContext.user match {
        case Some(user) => user
        case None => null
      }
    }
  }

  @PostMapping(value = Array("/api/oauth2/logout"))
  @ResponseBody
  def logout(response: HttpServletResponse): ResponseEntity[_] = {
    api.execute("logout") {
      response.addCookie(knooppuntnetResetCookie())
      new ResponseEntity[String]("", HttpStatus.OK)
    }
  }

  private def knooppuntnetResetCookie(): Cookie = {
    val cookie = new Cookie(AuthenticationConfiguration.cookieName, null)
    cookie.setMaxAge(0) // a zero value here will delete the cookie
    cookie.setHttpOnly(true)
    cookie.setPath("/")
    cookie
  }
}
