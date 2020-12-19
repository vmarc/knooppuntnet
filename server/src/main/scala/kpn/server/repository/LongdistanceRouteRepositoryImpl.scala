package kpn.server.repository

import kpn.api.common.changes.details.ChangeKeyI
import kpn.api.common.monitor.LongdistanceRoute
import kpn.api.common.monitor.LongdistanceRouteChange
import kpn.core.database.Database
import kpn.core.database.doc.LongdistanceRouteChangeDoc
import kpn.core.database.doc.LongdistanceRouteDoc
import kpn.core.database.query.Query
import kpn.core.database.views.analyzer.AnalyzerDesign
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.KeyPrefix
import kpn.core.util.Log
import org.springframework.stereotype.Component

object LongdistanceRouteRepositoryImpl {

  case class ViewResultRow(doc: LongdistanceRouteDoc)

  case class ViewResult(rows: Seq[ViewResultRow])

  case class ChangeViewRow(doc: LongdistanceRouteChangeDoc)

  case class ChangeViewResult(total_rows: Long, offset: Option[Long], rows: Seq[ChangeViewRow])

}

@Component
class LongdistanceRouteRepositoryImpl(
  analysisDatabase: Database
) extends LongdistanceRouteRepository {

  private val log = Log(classOf[LongdistanceRouteRepositoryImpl])

  override def save(route: LongdistanceRoute): Unit = {
    log.debugElapsed {
      analysisDatabase.save(LongdistanceRouteDoc(docId(route.id), route))
      (s"Save route ${route.id}", ())
    }
  }

  override def routeWithId(routeId: Long): Option[LongdistanceRoute] = {
    analysisDatabase.docWithId(docId(routeId), classOf[LongdistanceRouteDoc]).map(_.longdistanceRoute)
  }

  override def all(): Seq[LongdistanceRoute] = {
    val query = Query(AnalyzerDesign, DocumentView, classOf[LongdistanceRouteRepositoryImpl.ViewResult])
      .startKey(s""""${KeyPrefix.LongdistanceRoute}"""")
      .endKey(s""""${KeyPrefix.LongdistanceRoute}-"""")
      .reduce(false)
      .includeDocs(true)
      .stale(true)
    val result = analysisDatabase.execute(query)
    result.rows.map(_.doc.longdistanceRoute)
  }

  override def saveChange(change: LongdistanceRouteChange): Unit = {
    log.debugElapsed {
      analysisDatabase.save(LongdistanceRouteChangeDoc(changeDocId(change.key), change))
      (s"Save route change ${change.key}", ())
    }
  }

  override def changes(): Seq[LongdistanceRouteChange] = {
//    val query = Query("_all_docs", classOf[LongdistanceRouteRepositoryImpl.ChangeViewResult])
//      .reduce(false)
//      .includeDocs(true)
//      .stale(true)
//    val result = analysisDatabase.execute(query)
//    result.rows.map(_.doc.longdistanceRouteChange)
    Seq()
  }

  override def change(routeId: Long, changeSetId: Long): Option[LongdistanceRouteChange] = {
    val docId = changeDocId(
      ChangeKeyI(
        1,
        null,
        changeSetId,
        routeId
      )
    )
    analysisDatabase.docWithId(docId, classOf[LongdistanceRouteChangeDoc]).map(_.longdistanceRouteChange)
  }

  private def docId(routeId: Long): String = {
    s"${KeyPrefix.LongdistanceRoute}:$routeId"
  }

  private def changeDocId(key: ChangeKeyI): String = {
    s"longdistance-change:${key.changeSetId}:${key.replicationNumber}:longdistance-route:${key.elementId}"
  }

}
