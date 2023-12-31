package kpn.server.config

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class ServerAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  override def onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication): Unit = {
    val session = request.getSession(false)
    if (session != null) {
      val returnUrl = session.getAttribute(ServerAuthenticationFilter.RETURN_URL)
      if (returnUrl != null) {
        session.removeAttribute(ServerAuthenticationFilter.RETURN_URL)
        response.sendRedirect("" + returnUrl)
      }
    }
    super.onAuthenticationSuccess(request, response, authentication)
  }
}
