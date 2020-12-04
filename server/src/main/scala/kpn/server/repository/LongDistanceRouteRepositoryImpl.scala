package kpn.server.repository

import kpn.api.common.longdistance.LongDistanceRoute
import kpn.core.database.Database
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
}

@Component
class LongDistanceRouteRepositoryImpl(
  analysisDatabase: Database
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

  private def docId(routeId: Long): String = {
    s"${KeyPrefix.LongDistanceRoute}:$routeId"
  }

}
