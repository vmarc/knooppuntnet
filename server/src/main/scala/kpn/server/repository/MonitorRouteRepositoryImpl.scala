package kpn.server.repository

import kpn.api.common.changes.details.ChangeKeyI
import kpn.api.common.monitor.MonitorRoute
import kpn.api.common.monitor.MonitorRouteChange
import kpn.core.database.Database
import kpn.core.database.doc.MonitorRouteChangeDoc
import kpn.core.database.doc.MonitorRouteDoc
import kpn.core.database.query.Query
import kpn.core.database.views.analyzer.AnalyzerDesign
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.KeyPrefix
import kpn.core.util.Log
import org.springframework.stereotype.Component

object MonitorRouteRepositoryImpl {

  case class ViewResultRow(doc: MonitorRouteDoc)

  case class ViewResult(rows: Seq[ViewResultRow])

  case class ChangeViewRow(doc: MonitorRouteChangeDoc)

  case class ChangeViewResult(total_rows: Long, offset: Option[Long], rows: Seq[ChangeViewRow])

}

@Component
class MonitorRouteRepositoryImpl(
  analysisDatabase: Database,
  monitorRouteChangeDatabase: Database
) extends MonitorRouteRepository {

  private val log = Log(classOf[MonitorRouteRepositoryImpl])

  override def save(route: MonitorRoute): Unit = {
    log.debugElapsed {
      analysisDatabase.save(MonitorRouteDoc(docId(route.id), route))
      (s"Save route ${route.id}", ())
    }
  }

  override def routeWithId(routeId: Long): Option[MonitorRoute] = {
    analysisDatabase.docWithId(docId(routeId), classOf[MonitorRouteDoc]).map(_.monitorRoute)
  }

  override def all(): Seq[MonitorRoute] = {

    val elementType = "monitor-route"

    val query = Query(AnalyzerDesign, DocumentView, classOf[MonitorRouteRepositoryImpl.ViewResult])
      .startKey(s""""$elementType"""")
      .endKey(s""""$elementType-"""")
      .reduce(false)
      .includeDocs(true)
      .stale(true)
    val result = analysisDatabase.execute(query)
    result.rows.map(_.doc.monitorRoute)
  }

  override def saveChange(change: MonitorRouteChange): Unit = {
    log.debugElapsed {
      monitorRouteChangeDatabase.save(MonitorRouteChangeDoc(changeDocId(change.key), change))
      (s"Save route change ${change.key}", ())
    }
  }

  override def changes(): Seq[MonitorRouteChange] = {
    val query = Query("_all_docs", classOf[MonitorRouteRepositoryImpl.ChangeViewResult])
      .reduce(false)
      .includeDocs(true)
      .stale(true)
    val result = monitorRouteChangeDatabase.execute(query)
    result.rows.map(_.doc.monitorRouteChange)
  }

  override def change(routeId: Long, changeSetId: Long): Option[MonitorRouteChange] = {
    val docId = changeDocId(
      ChangeKeyI(
        1,
        null,
        changeSetId,
        routeId
      )
    )
    monitorRouteChangeDatabase.docWithId(docId, classOf[MonitorRouteChangeDoc]).map(_.monitorRouteChange)
  }

  private def docId(routeId: Long): String = {
    s"${KeyPrefix.MonitorRoute}:$routeId"
  }

  private def changeDocId(key: ChangeKeyI): String = {
    s"change:${key.changeSetId}:${key.replicationNumber}:monitor-route:${key.elementId}"
  }

}
