package kpn.server.api

import javax.servlet.http.HttpServletRequest

trait Api {

  def execute[T](request: HttpServletRequest, action: String)(f: => T): T = {
    execute(request, None, action, "")(f)
  }

  def execute[T](request: HttpServletRequest, user: Option[String], action: String, args: String)(f: => T): T

}
