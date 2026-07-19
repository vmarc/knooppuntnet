package kpn.server.config

object RequestContext {
  val instance: ThreadLocal[Option[RequestContext]] = ThreadLocal.withInitial[Option[RequestContext]](() => None)

  def isLoggedIn: Boolean = {
    instance.get().flatMap(_.user).isDefined
  }

  def user: Option[String] = {
    instance.get().flatMap(_.user)
  }
}

case class RequestContext(
  remoteAddress: String,
  userAgentString: Option[String],
  deviceClass: Option[String],
  deviceName: Option[String],
  user: Option[String]
)