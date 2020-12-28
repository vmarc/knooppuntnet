package kpn.server.repository

import kpn.api.common.changes.details.ChangeKeyI
import kpn.core.database.Database
import kpn.core.database.doc.MonitorRouteChangeDoc
import kpn.core.database.doc.MonitorRouteChangeGeometryDoc
import kpn.core.database.doc.MonitorRouteDoc
import kpn.core.database.doc.MonitorRouteReferenceDoc
import kpn.core.database.doc.MonitorRouteStateDoc
import kpn.core.database.views.monitor.MonitorRouteReferenceView
import kpn.core.database.views.monitor.MonitorRouteView
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.springframework.stereotype.Component

@Component
class MonitorAdminRouteRepositoryImpl(
  monitorAdminDatabase: Database
) extends MonitorAdminRouteRepository {

  private val log = Log(classOf[MonitorAdminRouteRepositoryImpl])

  override def allRouteIds: Seq[Long] = {
    log.debugElapsed {
      val routeIds = MonitorRouteView.allRouteIds(monitorAdminDatabase, stale = false)
      (s"admin-all-route-ids", routeIds)
    }
  }

  override def saveRoute(route: MonitorRoute): Unit = {
    log.debugElapsed {
      val docId = MonitorDocId.routeDocId(route.id)
      monitorAdminDatabase.save(MonitorRouteDoc(docId, route))
      (s"Save route $docId", ())
    }
  }

  override def saveRouteState(routeState: MonitorRouteState): Unit = {
    log.debugElapsed {
      val docId = MonitorDocId.routeStateDocId(routeState.routeId)
      monitorAdminDatabase.save(MonitorRouteStateDoc(docId, routeState))
      (s"Save $docId", ())
    }
  }

  override def saveRouteReference(routeReference: MonitorRouteReference): Unit = {
    log.debugElapsed {
      val docId = MonitorDocId.routeReferenceDocId(routeReference.routeId, routeReference.key)
      monitorAdminDatabase.save(MonitorRouteReferenceDoc(docId, routeReference))
      (s"Save route reference $docId", ())
    }
  }

  override def saveRouteChange(routeChange: MonitorRouteChange): Unit = {
    log.debugElapsed {
      val docId = MonitorDocId.routeChangeDocId(routeChange.key)
      monitorAdminDatabase.save(MonitorRouteChangeDoc(docId, routeChange))
      (s"Save $docId", ())
    }
  }

  override def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit = {
    log.debugElapsed {
      val docId = MonitorDocId.routeChangeGeometryDocId(routeChangeGeometry.key)
      monitorAdminDatabase.save(MonitorRouteChangeGeometryDoc(docId, routeChangeGeometry))
      (s"Save $docId", ())
    }
  }

  override def route(routeId: Long): Option[MonitorRoute] = {
    monitorAdminDatabase.docWithId(
      MonitorDocId.routeDocId(routeId),
      classOf[MonitorRouteDoc]
    ).map(_.monitorRoute)
  }

  override def routeState(routeId: Long): Option[MonitorRouteState] = {
    monitorAdminDatabase.docWithId(
      MonitorDocId.routeStateDocId(routeId),
      classOf[MonitorRouteStateDoc]
    ).map(_.monitorRouteState)
  }

  override def routeReference(routeId: Long, key: String): Option[MonitorRouteReference] = {
    monitorAdminDatabase.docWithId(
      MonitorDocId.routeReferenceDocId(routeId, key),
      classOf[MonitorRouteReferenceDoc]
    ).map(_.monitorRouteReference)
  }

  override def routeChange(changeKey: ChangeKeyI): Option[MonitorRouteChange] = {
    monitorAdminDatabase.docWithId(
      MonitorDocId.routeChangeDocId(changeKey),
      classOf[MonitorRouteChangeDoc]
    ).map(_.monitorRouteChange)
  }

  override def routeChangeGeometry(changeKey: ChangeKeyI): Option[MonitorRouteChangeGeometry] = {
    monitorAdminDatabase.docWithId(
      MonitorDocId.routeChangeDocId(changeKey),
      classOf[MonitorRouteChangeGeometryDoc]
    ).map(_.monitorRouteChangeGeometry)
  }

  def routeReferenceKey(routeId: Long): Option[String] = {
    MonitorRouteReferenceView.reference(monitorAdminDatabase, routeId)
  }

}
