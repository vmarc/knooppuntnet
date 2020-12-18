package kpn.server.repository

import kpn.api.common.monitor.MonitorRouteGroup
import kpn.core.database.Database
import kpn.core.database.doc.MonitorRouteGroupDoc
import kpn.core.database.query.Query
import kpn.core.database.views.analyzer.AnalyzerDesign
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.KeyPrefix
import kpn.core.util.Log
import org.springframework.stereotype.Component

object MonitorRouteGroupRepositoryImpl {

  case class ViewResultRow(doc: MonitorRouteGroupDoc)

  case class ViewResult(rows: Seq[ViewResultRow])

}

@Component
class MonitorRouteGroupRepositoryImpl(monitorDatabase: Database) extends MonitorRouteGroupRepository {

  private val log = Log(classOf[MonitorRouteGroupRepositoryImpl])

  def all(): Seq[MonitorRouteGroup] = {
    val query = Query(AnalyzerDesign, DocumentView, classOf[MonitorRouteGroupRepositoryImpl.ViewResult])
      .startKey(s""""${KeyPrefix.MonitorRouteGroup}"""")
      .endKey(s""""${KeyPrefix.MonitorRouteGroup}-"""")
      .reduce(false)
      .includeDocs(true)
      .stale(true)
    val result = monitorDatabase.execute(query)
    result.rows.map(_.doc.routeGroup)
  }

  private def docId(routeGroupId: String): String = {
    s"${KeyPrefix.MonitorRouteGroup}:$routeGroupId"
  }

}
