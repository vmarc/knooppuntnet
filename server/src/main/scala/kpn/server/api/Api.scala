package kpn.server.api

trait Api {

  def execute[T](action: String)(f: => T): T = {
    execute(None, action, "")(f)
  }

  def execute[T](user: Option[String], action: String, args: String)(f: => T): T

}
