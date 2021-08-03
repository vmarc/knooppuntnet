package kpn.server.repository

import kpn.api.common.monitor.MonitorGroup
import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

@Component
class MonitorAdminGroupRepositoryImpl(database: Database) extends MonitorAdminGroupRepository {

  private val log = Log(classOf[MonitorGroupRepositoryImpl])

  def groups(): Seq[MonitorGroup] = {
    database.monitorGroups.findAll(log)
  }

  def group(groupName: String): Option[MonitorGroup] = {
    database.monitorGroups.findByStringId(groupName, log)
  }

  def saveGroup(routeGroup: MonitorGroup): Unit = {
    database.monitorGroups.save(routeGroup, log)
  }

  def deleteGroup(name: String): Unit = {
    database.monitorGroups.deleteByStringId(name, log)
  }

  def groupRoutes(groupName: String): Seq[MonitorRoute] = {
    database.monitorRoutes.find(
      filter(
        equal("groupName", groupName)
      ),
      log
    )
  }
}
