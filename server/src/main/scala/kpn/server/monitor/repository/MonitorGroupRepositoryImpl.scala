package kpn.server.monitor.repository

import kpn.api.base.ObjectId
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.ObjectIdId
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.OldMonitorRoute
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy
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
    val pipeline = Seq(
      filter(
        equal("groupId", groupId.raw)
      ),
      project(
        include("_id")
      )
    )
    val routeIds = database.monitorRoutes.aggregate[ObjectIdId](pipeline, log).map(_._id)
    val delete = new MonitorRouteDelete(database)
    routeIds.foreach { routeId =>
      delete.delete(routeId, log)
    }
  }

  override def groupRoutes(groupId: ObjectId): Seq[MonitorRoute] = {
    database.monitorRoutes.find[MonitorRoute](
      equal("groupId", groupId.raw),
      log
    )
  }

  override def groupRouteIds(groupId: ObjectId): Seq[ObjectId] = {
    val pipeline = Seq(
      filter(
        equal("groupId", groupId.raw)
      ),
      sort(orderBy(ascending("name"))),
      project(
        include("_id")
      )
    )
    database.monitorRoutes.aggregate[ObjectIdId](pipeline, log).map(_._id)
  }

  override def oldGroupRoutes(groupId: ObjectId): Seq[OldMonitorRoute] = {
    database.oldMonitorRoutes.find[OldMonitorRoute](
      equal("groupId", groupId.raw),
      log
    )
  }
}
