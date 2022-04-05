package kpn.server.repository

import kpn.api.common.common.User

trait MonitorRepository {

  def adminUsers(): Seq[User]

  def isAdminUser(name: Option[String]): Boolean

}
