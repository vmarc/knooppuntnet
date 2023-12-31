package kpn.server.config

import org.springframework.stereotype.Component

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

object ServerAuthenticationFilter {
  val RETURN_URL = "RETURN_URL"
}

@Component
class ServerAuthenticationFilter extends HttpFilter {

  override def doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain): Unit = {
    if (request.getRequestURI.contains("/oauth2/authorization/osm")) {
      val session = request.getSession
      val returnUrl = request.getHeader("Referer")
      if (session != null && returnUrl != null) {
        session.setAttribute(ServerAuthenticationFilter.RETURN_URL, returnUrl)
      }
    }
    chain.doFilter(request, response)
  }
}
