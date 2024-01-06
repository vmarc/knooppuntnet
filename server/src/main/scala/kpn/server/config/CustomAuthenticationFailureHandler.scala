package kpn.server.config

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationFailureHandler extends AuthenticationFailureHandler {

  override def onAuthenticationFailure(
    request: HttpServletRequest,
    response: HttpServletResponse,
    exception: AuthenticationException
  ): Unit = {
    val session = request.getSession(false)
    if (session != null) {
      val returnUrl = session.getAttribute(AuthenticationConfiguration.returnUrlKey).toString
      if (returnUrl != null) {
        session.removeAttribute(AuthenticationConfiguration.returnUrlKey)
        session.invalidate()
        response.addCookie(resetCookie(AuthenticationConfiguration.cookieName))
        response.addCookie(resetCookie("JSESSIONID"))
        response.sendRedirect(returnUrl)
      }
    }
  }

  private def resetCookie(name: String): Cookie = {
    val sessionCookie = new Cookie(name, null)
    sessionCookie.setMaxAge(0) // a zero value here will delete the cookie
    sessionCookie.setPath("/")
    sessionCookie
  }
}
