package kpn.backend

import javax.servlet.FilterChain
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object ServerAuthenticationFilter {
  val RETURN_URL = "RETURN_URL"
}

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
