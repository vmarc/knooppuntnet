package kpn.server.repository

import kpn.api.common.changes.details.ChangeKeyI
import kpn.api.common.longdistance.LongDistanceRoute
import kpn.api.common.longdistance.LongDistanceRouteChange
import kpn.core.database.Database
import kpn.core.database.doc.LongDistanceRouteChangeDoc
import kpn.core.database.doc.LongDistanceRouteDoc
import kpn.core.database.query.Query
import kpn.core.database.views.analyzer.AnalyzerDesign
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.KeyPrefix
import kpn.core.util.Log
import org.springframework.stereotype.Component

object LongDistanceRouteRepositoryImpl {

  case class ViewResultRow(doc: LongDistanceRouteDoc)

  case class ViewResult(rows: Seq[ViewResultRow])

  case class ChangeViewRow(doc: LongDistanceRouteChangeDoc)

  case class ChangeViewResult(total_rows: Long, offset: Option[Long], rows: Seq[ChangeViewRow])

}

@Component
class LongDistanceRouteRepositoryImpl(
  analysisDatabase: Database,
  longDistanceRouteChangeDatabase: Database
) extends LongDistanceRouteRepository {

  private val log = Log(classOf[LongDistanceRouteRepositoryImpl])

  override def save(route: LongDistanceRoute): Unit = {
    log.debugElapsed {
      analysisDatabase.save(LongDistanceRouteDoc(docId(route.id), route))
      (s"Save route ${route.id}", ())
    }
  }

  override def routeWithId(routeId: Long): Option[LongDistanceRoute] = {
    analysisDatabase.docWithId(docId(routeId), classOf[LongDistanceRouteDoc]).map(_.route)
  }

  override def all(): Seq[LongDistanceRoute] = {

    val elementType = "long-distance-route"

    val query = Query(AnalyzerDesign, DocumentView, classOf[LongDistanceRouteRepositoryImpl.ViewResult])
      .startKey(s""""$elementType"""")
      .endKey(s""""$elementType-"""")
      .reduce(false)
      .includeDocs(true)
      .stale(true)
    val result = analysisDatabase.execute(query)
    result.rows.map(_.doc.route)
  }

  override def saveChange(change: LongDistanceRouteChange): Unit = {
    log.debugElapsed {
      analysisDatabase.save(LongDistanceRouteChangeDoc(changeDocId(change.key), change))
      (s"Save route change ${change.key}", ())
    }
  }

  override def changes(): Seq[LongDistanceRouteChange] = {
    val query = Query("_all_docs", classOf[LongDistanceRouteRepositoryImpl.ChangeViewResult])
      .reduce(false)
      .includeDocs(true)
      .stale(true)
    val result = longDistanceRouteChangeDatabase.execute(query)
    result.rows.map(_.doc.longDistanceRouteChange)
  }

  override def change(routeId: Long, changeSetId: Long): Option[LongDistanceRouteChange] = {
    val docId = changeDocId(
      ChangeKeyI(
        1,
        null,
        changeSetId,
        routeId
      )
    )
    longDistanceRouteChangeDatabase.docWithId(docId, classOf[LongDistanceRouteChangeDoc]).map(_.longDistanceRouteChange)
  }

  private def docId(routeId: Long): String = {
    s"${KeyPrefix.LongDistanceRoute}:$routeId"
  }

  private def changeDocId(key: ChangeKeyI): String = {
    s"change:${key.changeSetId}:${key.replicationNumber}:long-distance-route:${key.elementId}"
  }

}
