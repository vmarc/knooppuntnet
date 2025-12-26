package kpn.server.monitor.repository

import kpn.api.common.common.User
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class MonitorUserRepository(database: Database) {

  def adminUsers(): Seq[User] = {
    database.users.findAll()
  }

  def isAdminUser(name: Option[String]): Boolean = {
    name.exists(userName => adminUsers().map(_._id).contains(userName))
  }
}
