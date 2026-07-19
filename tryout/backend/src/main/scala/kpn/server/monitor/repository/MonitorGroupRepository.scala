package kpn.server.monitor.repository

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.ObjectIdId
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorGroupRepository(database: Database) {

  private val log = Log(classOf[MonitorGroupRepository])

  def groupByName(groupName: String): Option[MonitorGroup] = {
    val pipeline = Seq(
      filter(
        equal("name", groupName)
      )
    )
    database.monitorGroups.optionAggregate(pipeline, classOf[MonitorGroup], log)
  }

  def groupById(groupId: ObjectId): Option[MonitorGroup] = {
    database.monitorGroups.findByObjectId(groupId, log)
  }

  def groups(): Seq[MonitorGroup] = {
    database.monitorGroups.findAll(log).sortBy(_.name)
  }

  def saveGroup(routeGroup: MonitorGroup): Unit = {
    database.monitorGroups.save(routeGroup, log)
  }

  def deleteGroup(groupId: ObjectId): Unit = {
    database.monitorGroups.deleteByObjectId(groupId, log)
    val pipeline = Seq(
      filter(
        equal("groupId", groupId)
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

  def groupRoutes(groupId: ObjectId): Seq[MonitorRoute] = {
    database.monitorRoutes.find(
      equal("groupId", groupId),
      log
    )
  }

  def groupRouteIds(groupId: ObjectId): Seq[ObjectId] = {
    val pipeline = Seq(
      filter(
        equal("groupId", groupId)
      ),
      sort(orderBy(ascending("name"))),
      project(
        include("_id")
      )
    )
    database.monitorRoutes.aggregate(pipeline, classOf[ObjectIdId], log).map(_._id)
  }
}
