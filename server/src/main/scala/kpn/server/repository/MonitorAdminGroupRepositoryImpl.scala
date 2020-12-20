package kpn.server.repository

import kpn.api.common.monitor.MonitorGroup
import kpn.core.database.Database
import kpn.core.database.doc.MonitorGroupDoc
import kpn.core.database.views.monitor.MonitorRouteView
import kpn.core.db.KeyPrefix
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import org.springframework.stereotype.Component

@Component
class MonitorAdminGroupRepositoryImpl(
  monitorAdminDatabase: Database
) extends MonitorAdminGroupRepository {

  private val log = Log(classOf[MonitorGroupRepositoryImpl])

  def groups(): Seq[MonitorGroup] = {
    log.debugElapsed {
      val groups = MonitorRouteView.groups(monitorAdminDatabase, stale = false)
      ("groups", groups)
    }
  }

  def group(groupName: String): Option[MonitorGroup] = {
    log.debugElapsed {
      val groupOption = monitorAdminDatabase.docWithId(docId(groupName), classOf[MonitorGroupDoc]).map(_.monitorGroup)
      (s"group $groupName", groupOption)
    }
  }

  def saveGroup(routeGroup: MonitorGroup): Unit = {
    log.debugElapsed {
      monitorAdminDatabase.save(MonitorGroupDoc(docId(routeGroup.name), routeGroup))
      (s"save-group ${routeGroup.name}", ())
    }
  }

  def deleteGroup(name: String): Unit = {
    log.debugElapsed {
      monitorAdminDatabase.deleteDocWithId(docId(name))
      (s"delete-group $name", ())
    }
  }

  def groupRoutes(groupName: String): Seq[MonitorRoute] = {
    log.debugElapsed {
      val routes = MonitorRouteView.groupRoutes(monitorAdminDatabase, groupName, stale = false)
      (s"group-routes $groupName", routes)
    }
  }

  private def docId(name: String): String = {
    s"${KeyPrefix.MonitorGroup}:$name"
  }

}
