package kpn.server.repository

import kpn.api.common.changes.details.ChangeKey
import kpn.core.database.doc.MonitorRouteChangeDoc
import kpn.core.database.doc.MonitorRouteChangeGeometryDoc
import kpn.core.database.doc.MonitorRouteDoc
import kpn.core.database.doc.MonitorRouteReferenceDoc
import kpn.core.database.doc.MonitorRouteStateDoc
import kpn.core.database.views.monitor.MonitorRouteReferenceView
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

@Component
class MonitorAdminRouteRepositoryImpl(
  database: Database,
  // old:
  monitorAdminDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends MonitorAdminRouteRepository {

  private val log = Log(classOf[MonitorAdminRouteRepositoryImpl])

  override def allRouteIds: Seq[Long] = {
    if (mongoEnabled) {
      database.monitorRoutes.ids(log)
    }
    else {
      log.debugElapsed {
        val routeIds = MonitorRouteView.allRouteIds(monitorAdminDatabase, stale = false)
        (s"admin-all-route-ids", routeIds)
      }
    }
  }

  override def saveRoute(route: MonitorRoute): Unit = {
    if (mongoEnabled) {
      database.monitorRoutes.save(route, log)
    }
    else {
      log.debugElapsed {
        val docId = MonitorDocId.routeDocId(route.id)
        monitorAdminDatabase.save(MonitorRouteDoc(docId, route))
        (s"Save route $docId", ())
      }
    }
  }

  override def saveRouteState(routeState: MonitorRouteState): Unit = {
    if (mongoEnabled) {
      database.monitorRouteStates.save(routeState, log)
    }
    else {
      log.debugElapsed {
        val docId = MonitorDocId.routeStateDocId(routeState.routeId)
        monitorAdminDatabase.save(MonitorRouteStateDoc(docId, routeState))
        (s"Save $docId", ())
      }
    }
  }

  override def saveRouteReference(routeReference: MonitorRouteReference): Unit = {
    if (mongoEnabled) {
      database.monitorRouteReferences.save(routeReference, log)
    }
    else {
      log.debugElapsed {
        val docId = MonitorDocId.routeReferenceDocId(routeReference.routeId, routeReference.key)
        monitorAdminDatabase.save(MonitorRouteReferenceDoc(docId, routeReference))
        (s"Save route reference $docId", ())
      }
    }
  }

  override def saveRouteChange(routeChange: MonitorRouteChange): Unit = {
    if (mongoEnabled) {
      database.monitorRouteChanges.save(routeChange, log)
    }
    else {
      log.debugElapsed {
        val docId = MonitorDocId.routeChangeDocId(routeChange.key)
        monitorAdminDatabase.save(MonitorRouteChangeDoc(docId, routeChange))
        (s"Save $docId", ())
      }
    }
  }

  override def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit = {
    if (mongoEnabled) {
      database.monitorRouteChangeGeometries.save(routeChangeGeometry, log)
    }
    else {
      log.debugElapsed {
        val docId = MonitorDocId.routeChangeGeometryDocId(routeChangeGeometry.key)
        monitorAdminDatabase.save(MonitorRouteChangeGeometryDoc(docId, routeChangeGeometry))
        (s"Save $docId", ())
      }
    }
  }

  override def route(routeId: Long): Option[MonitorRoute] = {
    if (mongoEnabled) {
      database.monitorRoutes.findById(routeId, log)
    }
    else {
      monitorAdminDatabase.docWithId(
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
      monitorAdminDatabase.docWithId(
        MonitorDocId.routeStateDocId(routeId),
        classOf[MonitorRouteStateDoc]
      ).map(_.monitorRouteState)
    }
  }

  override def routeReference(routeId: Long, key: String): Option[MonitorRouteReference] = {
    if (mongoEnabled) {
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
    else {
      monitorAdminDatabase.docWithId(
        MonitorDocId.routeReferenceDocId(routeId, key),
        classOf[MonitorRouteReferenceDoc]
      ).map(_.monitorRouteReference)
    }
  }

  override def routeChange(changeKey: ChangeKey): Option[MonitorRouteChange] = {
    if (mongoEnabled) {
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
    else {
      monitorAdminDatabase.docWithId(
        MonitorDocId.routeChangeDocId(changeKey),
        classOf[MonitorRouteChangeDoc]
      ).map(_.monitorRouteChange)
    }
  }

  override def routeChangeGeometry(changeKey: ChangeKey): Option[MonitorRouteChangeGeometry] = {
    if (mongoEnabled) {
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
    else {
      monitorAdminDatabase.docWithId(
        MonitorDocId.routeChangeDocId(changeKey),
        classOf[MonitorRouteChangeGeometryDoc]
      ).map(_.monitorRouteChangeGeometry)
    }
  }

  def routeReferenceKey(routeId: Long): Option[String] = {
    if (mongoEnabled) {
      // TODO MONGO should be looking for most recent entry here, instead of assuming there is always exactly 1 entry ???
      database.monitorRouteReferences.findOne(
        filter(
          equal("routeId", routeId),
        ),
        log
      )
    }
    else {
      MonitorRouteReferenceView.reference(monitorAdminDatabase, routeId)
    }
  }
}
