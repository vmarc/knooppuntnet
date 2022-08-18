package kpn.server.repository

import kpn.api.base.MongoId
import kpn.api.common.monitor.MonitorGroup
import kpn.core.util.Log
import kpn.database.base.Database
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

  override def groupById(groupId: MongoId): Option[MonitorGroup] = {
    database.monitorGroups.findByMongoId(groupId, log)
  }

  override def groups(): Seq[MonitorGroup] = {
    database.monitorGroups.findAll(log)
  }

  override def saveGroup(routeGroup: MonitorGroup): Unit = {
    database.monitorGroups.save(routeGroup, log)
  }

  override def deleteGroup(groupId: MongoId): Unit = {
    database.monitorGroups.deleteByMongoId(groupId, log)
  }

  override def groupRoutes(groupId: MongoId): Seq[MonitorRoute] = {
    database.monitorRoutes.find[MonitorRoute](
      equal("groupId", groupId.toObjectId),
      log
    )
  }
}
