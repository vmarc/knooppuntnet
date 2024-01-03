package kpn.server.config

import org.springframework.stereotype.Component

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/*
  Intercepts the {@code /oauth2/authorization/osm} login request, to extract {@code Referer} http header value
  and store that on the session, so that it can be used to url to return to after succesful login.
 */
@Component
class AuthenticationReturnUrlFilter extends HttpFilter {

  override def doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain): Unit = {
    if (request.getRequestURI.contains("/oauth2/authorization/osm")) {
      val session = request.getSession
      val returnUrl = request.getHeader("Referer")
      if (session != null && returnUrl != null) {
        session.setAttribute(AuthenticationConfiguration.returnUrlKey, returnUrl)
      }
    }
    chain.doFilter(request, response)
  }
}
