package kpn.server.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component

@Component
class ServerAuthenticationLogoutSuccessHandler extends LogoutSuccessHandler {

  override def onLogoutSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ): Unit = {
    // the default is redirect to 'logoutSuccessUrl", but we just want http 200 OK status
  }
}
