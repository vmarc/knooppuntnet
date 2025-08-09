package kpn.server.monitor.repository

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.base.ObjectId
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.ObjectIdId
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
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
    database.monitorGroups.optionAggregate(pipeline, classOf[MonitorGroup], log)
  }

  override def groupById(groupId: ObjectId): Option[MonitorGroup] = {
    database.monitorGroups.findByObjectId(groupId, log)
  }

  override def groups(): Seq[MonitorGroup] = {
    database.monitorGroups.findAll(log).sortBy(_.name)
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
    val routeIds = database.monitorRoutes.aggregate(pipeline, classOf[ObjectIdId], log).map(_._id)
    val delete = new MonitorRouteDelete(database)
    routeIds.foreach { routeId =>
      delete.delete(routeId, log)
    }
  }

  override def groupRoutes(groupId: ObjectId): Seq[MonitorRoute] = {
    database.monitorRoutes.find(
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
    database.monitorRoutes.aggregate(pipeline, classOf[ObjectIdId], log).map(_._id)
  }
}
