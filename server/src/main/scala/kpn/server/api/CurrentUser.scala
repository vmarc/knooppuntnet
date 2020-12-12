package kpn.server.api

import org.springframework.security.core.context.SecurityContextHolder

object CurrentUser {

  def name: Option[String] = {
    val authentication = SecurityContextHolder.getContext.getAuthentication
    if (authentication != null) {
      Some(authentication.getName)
    }
    else {
      None
    }
  }
}
