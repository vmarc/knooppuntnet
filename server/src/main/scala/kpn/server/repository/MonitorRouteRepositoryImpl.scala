package kpn.server.repository

import kpn.api.common.monitor.MonitorChangesParameters
import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

@Component
class MonitorRouteRepositoryImpl(database: Database) extends MonitorRouteRepository {

  private val log = Log(classOf[NetworkRepositoryImpl])

  override def route(routeId: Long): Option[MonitorRoute] = {
    database.monitorRoutes.findById(routeId, log)
  }

  override def routeState(routeId: Long): Option[MonitorRouteState] = {
    database.monitorRouteStates.findById(routeId, log)
  }

  override def routeReference(routeId: Long, key: String): Option[MonitorRouteReference] = {
    database.monitorRouteReferences.findByStringId(s"$routeId:$key", log)
  }

  override def routeChange(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange] = {
    val _id = s"$routeId:$changeSetId:$replicationNumber"
    database.monitorRouteChanges.findByStringId(_id, log)
  }

  override def routeChangeGeometry(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry] = {
    val _id = s"$routeId:$changeSetId:$replicationNumber"
    database.monitorRouteChangeGeometries.findByStringId(_id, log)
  }

  override def changesCount(parameters: MonitorChangesParameters): Long = {
    if (parameters.impact) {
      database.monitorRouteChanges.countDocuments(
        filter(
          equal("impact", true)
        ),
        log
      )
    }
    else {
      database.monitorRouteChanges.countDocuments(log)
    }
  }

  override def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    // TODO MONGO add query with aggregation
    database.monitorRouteChanges.findAll(log)
  }

  override def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long = {
    val changesFilter = if (parameters.impact) {
      filter(
        and(
          equal("groupName", groupName),
          equal("impact", parameters.impact)
        )
      )
    }
    else {
      filter(
        equal("groupName", groupName),
      )
    }
    database.monitorRouteChanges.countDocuments(changesFilter, log)
  }

  override def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    // TODO MONGO create query with aggregate
    val changesFilter = if (parameters.impact) {
      filter(
        and(
          equal("groupName", groupName),
          equal("impact", parameters.impact)
        )
      )
    }
    else {
      filter(
        equal("groupName", groupName),
      )
    }
    database.monitorRouteChanges.find(changesFilter, log)
  }

  override def routeChangesCount(routeId: Long, parameters: MonitorChangesParameters): Long = {
    val changesFilter = if (parameters.impact) {
      filter(
        and(
          equal("routeId", routeId),
          equal("impact", parameters.impact)
        )
      )
    }
    else {
      filter(
        equal("routeId", routeId)
      )
    }
    database.monitorRouteChanges.countDocuments(changesFilter, log)
  }

  override def routeChanges(routeId: Long, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    // TODO MONGO create query with aggregate
    val changesFilter = if (parameters.impact) {
      filter(
        and(
          equal("routeId", routeId),
          equal("impact", parameters.impact)
        )
      )
    }
    else {
      filter(
        equal("routeId", routeId)
      )
    }
    database.monitorRouteChanges.find(changesFilter, log)
  }

  override def routes(): Seq[MonitorRoute] = {
    database.monitorRoutes.findAll(log)
  }
}
