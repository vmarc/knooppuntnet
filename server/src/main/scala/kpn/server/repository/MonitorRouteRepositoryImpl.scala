package kpn.server.repository

import kpn.api.common.monitor.MonitorChangesParameters
import kpn.core.database.doc.MonitorRouteChangeDoc
import kpn.core.database.doc.MonitorRouteChangeGeometryDoc
import kpn.core.database.doc.MonitorRouteDoc
import kpn.core.database.doc.MonitorRouteReferenceDoc
import kpn.core.database.doc.MonitorRouteStateDoc
import kpn.core.database.views.monitor.MonitorChangesView
import kpn.core.database.views.monitor.MonitorRouteView
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

object MonitorRouteRepositoryImpl {

  case class ViewResultRow(doc: MonitorRouteDoc)

  case class ViewResult(rows: Seq[ViewResultRow])

  case class ChangeViewRow(doc: MonitorRouteChangeDoc)

  case class ChangeViewResult(total_rows: Long, offset: Option[Long], rows: Seq[ChangeViewRow])

}

@Component
class MonitorRouteRepositoryImpl(
  database: Database,
  // old:
  monitorDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends MonitorRouteRepository {

  private val log = Log(classOf[NetworkRepositoryImpl])

  override def route(routeId: Long): Option[MonitorRoute] = {
    if (mongoEnabled) {
      database.monitorRoutes.findById(routeId, log)
    }
    else {
      monitorDatabase.docWithId(
        MonitorDocId.routeDocId(routeId),
        classOf[MonitorRouteDoc]
      ).map(_.monitorRoute)
    }
  }

  override def routeState(routeId: Long): Option[MonitorRouteState] = {
    if (mongoEnabled) {
      database.monitorRouteStates.findById(routeId, log)
    }
    else {
      monitorDatabase.docWithId(
        MonitorDocId.routeStateDocId(routeId),
        classOf[MonitorRouteStateDoc]
      ).map(_.monitorRouteState)
    }
  }

  override def routeReference(routeId: Long, key: String): Option[MonitorRouteReference] = {
    if (mongoEnabled) {
      database.monitorRouteReferences.findByStringId(s"$routeId:$key", log)
    }
    else {
      monitorDatabase.docWithId(
        MonitorDocId.routeReferenceDocId(routeId, key),
        classOf[MonitorRouteReferenceDoc]
      ).map(_.monitorRouteReference)
    }
  }

  override def routeChange(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange] = {
    if (mongoEnabled) {
      val _id = s"$routeId:$changeSetId:$replicationNumber"
      database.monitorRouteChanges.findByStringId(_id, log)
    }
    else {
      monitorDatabase.docWithId(
        MonitorDocId.routeChangeDocId(routeId, changeSetId, replicationNumber),
        classOf[MonitorRouteChangeDoc]
      ).map(_.monitorRouteChange)
    }
  }

  override def routeChangeGeometry(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry] = {
    if (mongoEnabled) {
      val _id = s"$routeId:$changeSetId:$replicationNumber"
      database.monitorRouteChangeGeometries.findByStringId(_id, log)
    }
    else {
      monitorDatabase.docWithId(
        MonitorDocId.routeChangeGeometryDocId(routeId, changeSetId, replicationNumber),
        classOf[MonitorRouteChangeGeometryDoc]
      ).map(_.monitorRouteChangeGeometry)
    }
  }

  override def changesCount(parameters: MonitorChangesParameters): Long = {
    if (mongoEnabled) {
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
    else {
      MonitorChangesView.changesCount(monitorDatabase, parameters)
    }
  }

  override def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    if (mongoEnabled) {
      // TODO MONGO add query with aggregation
      database.monitorRouteChanges.findAll(log)
    }
    else {
      MonitorChangesView.changes(monitorDatabase, parameters)
    }
  }

  override def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long = {
    if (mongoEnabled) {
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
    else {
      MonitorChangesView.groupChangesCount(monitorDatabase, groupName, parameters)
    }
  }

  override def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    if (mongoEnabled) {
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
    else {
      MonitorChangesView.groupChanges(monitorDatabase, groupName, parameters)
    }
  }

  override def routeChangesCount(routeId: Long, parameters: MonitorChangesParameters): Long = {
    if (mongoEnabled) {
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
    else {
      MonitorChangesView.routeChangesCount(monitorDatabase, routeId, parameters)
    }
  }

  override def routeChanges(routeId: Long, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    if (mongoEnabled) {
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
    else {
      MonitorChangesView.routeChanges(monitorDatabase, routeId, parameters)
    }
  }

  override def routes(): Seq[MonitorRoute] = {
    if (mongoEnabled) {
      database.monitorRoutes.findAll(log)
    }
    else {
      MonitorRouteView.routes(monitorDatabase)
    }
  }
}
