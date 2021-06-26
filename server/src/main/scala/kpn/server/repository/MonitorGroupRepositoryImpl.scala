package kpn.server.repository

import kpn.api.common.monitor.MonitorGroup
import kpn.core.database.doc.MonitorGroupDoc
import kpn.core.database.views.monitor.MonitorRouteView
import kpn.core.db.KeyPrefix
import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

@Component
class MonitorGroupRepositoryImpl(
  database: Database,
  // old
  monitorDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends MonitorGroupRepository {

  private val log = Log(classOf[MonitorGroupRepositoryImpl])

  override def group(groupName: String): Option[MonitorGroup] = {
    if (mongoEnabled) {
      database.monitorGroups.findByStringId(groupName)
    }
    else {
      log.debugElapsed {
        val groupOption = monitorDatabase.docWithId(docId(groupName), classOf[MonitorGroupDoc]).map(_.monitorGroup)
        (s"group $groupName", groupOption)
      }
    }
  }

  override def groups(): Seq[MonitorGroup] = {
    if (mongoEnabled) {
      database.monitorGroups.findAll()
    }
    else {
      log.debugElapsed {
        val groups = MonitorRouteView.groups(monitorDatabase)
        (s"groups", groups)
      }
    }
  }

  override def groupRoutes(groupName: String): Seq[MonitorRoute] = {
    if (mongoEnabled) {
      database.monitorRoutes.find(
        filter(
          equal("groupName", groupName)
        )
      )
    }
    else {
      log.debugElapsed {
        val routes = MonitorRouteView.groupRoutes(monitorDatabase, groupName)
        (s"group routes $groupName", routes)
      }
    }
  }

  private def docId(name: String): String = {
    s"${KeyPrefix.MonitorGroup}:$name"
  }
}
