package kpn.server.monitor.repository

import kpn.api.common.common.User

trait MonitorUserRepository {

  def adminUsers(): Seq[User]

  def isAdminUser(name: Option[String]): Boolean
}
