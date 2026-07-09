package kpn.server.api

trait Api {

  def execute[T](action: String)(f: => T): T = {
    execute(action, "")(f)
  }

  def execute[T](action: String, args: String)(f: => T): T

}
