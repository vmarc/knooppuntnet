package kpn.server.repository

import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.mongodb.scala.model.Aggregates.addFields
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

  override def route(routeId: Long): Option[MonitorRoute] = {
    database.monitorRoutes.findById(routeId, log)
  }

  override def routeState(routeId: Long): Option[MonitorRouteState] = {
    database.monitorRouteStates.findById(routeId, log)
  }

  override def routeReference(routeId: Long, key: String): Option[MonitorRouteReference] = {
    database.monitorRouteReferences.findOne(
      filter(
        and(
          equal("_id", routeId),
          equal("key", key)
        )
      ),
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

  override def routeReferenceKey(routeId: Long): Option[String] = {
    // TODO MONGO should be looking for most recent entry here, instead of assuming there is always exactly 1 entry ???
    database.monitorRouteReferences.findOne(
      filter(
        equal("routeId", routeId),
      ),
      log
    )
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
          or(
            equal("happy", true),
            equal("investigate", true)
          )
        ),
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
      Some(skip((parameters.pageIndex * parameters.itemsPerPage).toInt)),
      Some(limit(parameters.itemsPerPage.toInt)),
      Some(
        addFields()
      )
    ).flatten


    println(Mongo.pipelineString(pipeline))

    log.debugElapsed {
      val changes = database.monitorRouteChanges.aggregate[MonitorRouteChange](pipeline, log)
      val result = s"changes: ${changes.size}"
      (result, changes)
    }
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
