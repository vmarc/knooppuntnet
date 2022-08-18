package kpn.server.repository

import kpn.api.base.ObjectId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.or
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy
import org.springframework.stereotype.Component

@Component
class MonitorRouteRepositoryImpl(database: Database) extends MonitorRouteRepository {

  private val log = Log(classOf[NetworkRepositoryImpl])

  override def allRouteIds: Seq[Long] = {
    database.monitorRoutes.ids(log)
  }

  override def saveRoute(route: MonitorRoute): Unit = {
    database.monitorRoutes.save(route, log)
  }

  override def deleteRoute(routeId: ObjectId): Unit = {
    // TODO MON
    //    database.monitorRoutes.deleteByObjectId(routeId, log)
    //    database.monitorRouteStates.deleteByObjectId(routeDocId, log)
    // TODO MON
    // database.monitorRouteReferences.find(routereferences) + delete
    //  database.monitorRouteChanges.delete ...
    //  database.monitorRouteChangeGeometries.delete ...
  }

  override def saveRouteState(routeState: MonitorRouteState): Unit = {
    database.monitorRouteStates.save(routeState, log)
  }

  override def saveRouteReference(routeReference: MonitorRouteReference): Unit = {
    database.monitorRouteReferences.save(routeReference, log)
  }

  override def saveRouteChange(routeChange: MonitorRouteChange): Unit = {
    database.monitorRouteChanges.save(routeChange, log)
  }

  override def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit = {
    database.monitorRouteChangeGeometries.save(routeChangeGeometry, log)
  }

  override def routeById(monitorRouteId: ObjectId): Option[MonitorRoute] = {
    database.monitorRoutes.findByObjectId(monitorRouteId, log)
  }

  override def routeState(monitorRouteId: String): Option[MonitorRouteState] = {
    database.monitorRouteStates.findByStringId(monitorRouteId, log)
  }

  override def routeReference(monitorRouteId: String, key: String): Option[MonitorRouteReference] = {
    database.monitorRouteReferences.findOne[MonitorRouteReference](
      equal("_id", s"$monitorRouteId:$key"),
      log
    )
  }

  override def routeReference(monitorRouteId: String): Option[MonitorRouteReference] = {
    database.monitorRouteReferences.findOne[MonitorRouteReference](
      equal("monitorRouteId", monitorRouteId),
      log
    )
  }

  override def routeChange(changeKey: ChangeKey): Option[MonitorRouteChange] = {
    database.monitorRouteChanges.findOne(
      filter(
        and(
          equal("key.elementId", changeKey.elementId),
          equal("key.changeSetId", changeKey.changeSetId),
          equal("key.replicationNumber", changeKey.replicationNumber)
        )
      ),
      log
    )
  }

  override def routeChangeGeometry(changeKey: ChangeKey): Option[MonitorRouteChangeGeometry] = {
    database.monitorRouteChangeGeometries.findOne(
      filter(
        and(
          equal("key.elementId", changeKey.elementId),
          equal("key.changeSetId", changeKey.changeSetId),
          equal("key.replicationNumber", changeKey.replicationNumber)
        )
      ),
      log
    )
  }

  override def routeReferenceKey(routeMonitorId: String): Option[String] = {
    // TODO MONGO should be looking for most recent entry here, instead of assuming there is always exactly 1 entry ???
    database.monitorRouteReferences.findOne(
      filter(
        equal("routeId", routeMonitorId),
      ),
      log
    )
  }

  override def routeChange(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange] = {
    val _id = s"$monitorRouteId:$changeSetId:$replicationNumber"
    database.monitorRouteChanges.findByStringId(_id, log)
  }

  override def routeChangeGeometry(monitorRouteId: String, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry] = {
    val _id = s"$monitorRouteId:$changeSetId:$replicationNumber"
    database.monitorRouteChangeGeometries.findByStringId(_id, log)
  }

  override def changesCount(parameters: MonitorChangesParameters): Long = {
    if (parameters.impact) {
      val filter = or(
        equal("happy", true),
        equal("investigate", true)
      )
      database.monitorRouteChanges.countDocuments(
        filter,
        log
      )
    }
    else {
      database.monitorRouteChanges.countDocuments(log)
    }
  }

  override def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    val pipeline = Seq(
      if (parameters.impact) {
        Some(
          filter(
            or(
              equal("happy", true),
              equal("investigate", true),
            )
          )
        )
      }
      else {
        None
      },
      Some(sort(orderBy(descending("key.time")))),
      Some(skip((parameters.pageSize * parameters.pageIndex).toInt)),
      Some(limit(parameters.pageSize.toInt))
    ).flatten

    log.debugElapsed {
      val changes = database.monitorRouteChanges.aggregate[MonitorRouteChange](pipeline, log)
      val result = s"changes: ${changes.size}"
      (result, changes)
    }
  }

  override def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long = {
    val changesFilter = groupChangesFilter(groupName, parameters)
    database.monitorRouteChanges.countDocuments(changesFilter, log)
  }

  override def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    val pipeline = Seq(
      filter(
        groupChangesFilter(groupName, parameters)
      ),
      sort(orderBy(descending("key.time"))),
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt)
    )
    database.monitorRouteChanges.aggregate[MonitorRouteChange](pipeline, log)
  }

  override def routeChangesCount(id: String, parameters: MonitorChangesParameters): Long = {
    val changesFilter = routeChangesCountFilter(id, parameters)
    database.monitorRouteChanges.countDocuments(changesFilter, log)
  }

  override def routeChanges(monitorRouteId: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    val pipeline = Seq(
      filter(
        routeChangesCountFilter(monitorRouteId, parameters)
      ),
      sort(orderBy(descending("key.time"))),
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt)
    )
    database.monitorRouteChanges.aggregate[MonitorRouteChange](pipeline, log)
  }

  private def groupChangesFilter(groupName: String, parameters: MonitorChangesParameters): Bson = {
    if (parameters.impact) {
      and(
        equal("groupName", groupName),
        or(
          equal("happy", true),
          equal("investigate", true)
        )
      )
    }
    else {
      equal("groupName", groupName)
    }
  }

  private def routeChangesCountFilter(monitorRouteId: String, parameters: MonitorChangesParameters): Bson = {
    if (parameters.impact) {
      and(
        equal("key.elementId", monitorRouteId),
        or(
          equal("happy", true),
          equal("investigate", true)
        )
      )
    }
    else {
      equal("key.elementId", monitorRouteId)
    }
  }

  override def routes(): Seq[MonitorRoute] = {
    database.monitorRoutes.findAll(log)
  }
}
