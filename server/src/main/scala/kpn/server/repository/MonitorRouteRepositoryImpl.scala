package kpn.server.repository

import kpn.api.common.monitor.MonitorChangesParameters
import kpn.core.database.Database
import kpn.core.database.doc.MonitorRouteChangeDoc
import kpn.core.database.doc.MonitorRouteChangeGeometryDoc
import kpn.core.database.doc.MonitorRouteDoc
import kpn.core.database.doc.MonitorRouteReferenceDoc
import kpn.core.database.doc.MonitorRouteStateDoc
import kpn.core.database.views.monitor.MonitorChangesView
import kpn.core.database.views.monitor.MonitorRouteView
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.springframework.stereotype.Component

object MonitorRouteRepositoryImpl {

  case class ViewResultRow(doc: MonitorRouteDoc)

  case class ViewResult(rows: Seq[ViewResultRow])

  case class ChangeViewRow(doc: MonitorRouteChangeDoc)

  case class ChangeViewResult(total_rows: Long, offset: Option[Long], rows: Seq[ChangeViewRow])

}

@Component
class MonitorRouteRepositoryImpl(
  monitorDatabase: Database
) extends MonitorRouteRepository {

  override def route(routeId: Long): Option[MonitorRoute] = {
    monitorDatabase.docWithId(
      MonitorDocId.routeDocId(routeId),
      classOf[MonitorRouteDoc]
    ).map(_.monitorRoute)
  }

  override def routeState(routeId: Long): Option[MonitorRouteState] = {
    monitorDatabase.docWithId(
      MonitorDocId.routeStateDocId(routeId),
      classOf[MonitorRouteStateDoc]
    ).map(_.monitorRouteState)
  }

  override def routeReference(routeId: Long, key: String): Option[MonitorRouteReference] = {
    monitorDatabase.docWithId(
      MonitorDocId.routeReferenceDocId(routeId, key),
      classOf[MonitorRouteReferenceDoc]
    ).map(_.monitorRouteReference)
  }

  override def routeChange(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange] = {
    monitorDatabase.docWithId(
      MonitorDocId.routeChangeDocId(routeId, changeSetId, replicationNumber),
      classOf[MonitorRouteChangeDoc]
    ).map(_.monitorRouteChange)
  }

  override def routeChangeGeometry(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry] = {
    monitorDatabase.docWithId(
      MonitorDocId.routeChangeGeometryDocId(routeId, changeSetId, replicationNumber),
      classOf[MonitorRouteChangeGeometryDoc]
    ).map(_.monitorRouteChangeGeometry)
  }

  override def changesCount(parameters: MonitorChangesParameters): Long = {
    MonitorChangesView.changesCount(monitorDatabase, parameters)
  }

  override def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    MonitorChangesView.changes(monitorDatabase, parameters)
  }

  override def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long = {
    MonitorChangesView.groupChangesCount(monitorDatabase, groupName, parameters)
  }

  override def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    MonitorChangesView.groupChanges(monitorDatabase, groupName, parameters)
  }

  override def routeChangesCount(routeId: Long, parameters: MonitorChangesParameters): Long = {
    MonitorChangesView.routeChangesCount(monitorDatabase, routeId, parameters)
  }

  override def routeChanges(routeId: Long, parameters: MonitorChangesParameters): Seq[MonitorRouteChange] = {
    MonitorChangesView.routeChanges(monitorDatabase, routeId, parameters)
  }

  override def routes(): Seq[MonitorRoute] = {
    MonitorRouteView.routes(monitorDatabase)
  }

}
