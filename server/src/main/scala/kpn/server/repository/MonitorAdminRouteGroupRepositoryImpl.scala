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

object MonitorAdminRouteGroupRepositoryImpl {

  case class ViewResultRow(doc: MonitorRouteGroupDoc)

  case class ViewResult(rows: Seq[ViewResultRow])

}

@Component
class MonitorAdminRouteGroupRepositoryImpl(
  monitorAdminDatabase: Database
) extends MonitorAdminRouteGroupRepository {

  private val log = Log(classOf[MonitorRouteGroupRepositoryImpl])

  def all(): Seq[MonitorRouteGroup] = {
    val query = Query(AnalyzerDesign, DocumentView, classOf[MonitorAdminRouteGroupRepositoryImpl.ViewResult])
      .startKey(s""""${KeyPrefix.MonitorRouteGroup}"""")
      .endKey(s""""${KeyPrefix.MonitorRouteGroup}-"""")
      .reduce(false)
      .includeDocs(true)
      .stale(true)
    val result = monitorAdminDatabase.execute(query)
    result.rows.map(_.doc.routeGroup)
  }

  def group(groupName: String): Option[MonitorRouteGroup] = {
    log.debugElapsed {
      val groupOption = monitorAdminDatabase.docWithId(docId(groupName), classOf[MonitorRouteGroupDoc]).map(_.routeGroup)
      (s"Get route group $groupName", groupOption)
    }
  }

  def saveGroup(routeGroup: MonitorRouteGroup): Unit = {
    log.debugElapsed {
      monitorAdminDatabase.save(MonitorRouteGroupDoc(docId(routeGroup.name), routeGroup))
      (s"Save route group ${routeGroup.name}", ())
    }
  }

  def deleteGroup(name: String): Unit = {
    log.debugElapsed {
      monitorAdminDatabase.deleteDocWithId(docId(name))
      (s"Delete route group $name", ())
    }
  }

  private def docId(name: String): String = {
    s"${KeyPrefix.MonitorRouteGroup}:$name"
  }

}
