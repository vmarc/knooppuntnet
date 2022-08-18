package kpn.server.repository

import kpn.api.base.ObjectId
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

@Component
class MonitorGroupRepositoryImpl(database: Database) extends MonitorGroupRepository {

  private val log = Log(classOf[MonitorGroupRepositoryImpl])

  override def groupByName(groupName: String): Option[MonitorGroup] = {
    val pipeline = Seq(
      filter(
        equal("name", groupName)
      )
    )
    database.monitorGroups.optionAggregate[MonitorGroup](pipeline, log)
  }

  override def groupById(groupId: ObjectId): Option[MonitorGroup] = {
    database.monitorGroups.findByObjectId(groupId, log)
  }

  override def groups(): Seq[MonitorGroup] = {
    database.monitorGroups.findAll(log)
  }

  override def saveGroup(routeGroup: MonitorGroup): Unit = {
    database.monitorGroups.save(routeGroup, log)
  }

  override def deleteGroup(groupId: ObjectId): Unit = {
    database.monitorGroups.deleteByObjectId(groupId, log)
  }

  override def groupRoutes(groupId: ObjectId): Seq[MonitorRoute] = {
    database.monitorRoutes.find[MonitorRoute](
      equal("groupId", groupId.raw),
      log
    )
  }
}
