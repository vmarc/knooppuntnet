package kpn.server.repository

import kpn.api.common.common.User
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class MonitorRepositoryImpl(database: Database) extends MonitorRepository {

  override def adminUsers(): Seq[User] = {
    database.users.findAll()
  }

  override def isAdminUser(name: Option[String]): Boolean = {
    name.exists(userName => adminUsers().map(_._id).contains(userName))
  }
}
