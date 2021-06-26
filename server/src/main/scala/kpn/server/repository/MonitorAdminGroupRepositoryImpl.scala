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
class MonitorAdminGroupRepositoryImpl(
  database: Database,
  // old:
  monitorAdminDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends MonitorAdminGroupRepository {

  private val log = Log(classOf[MonitorGroupRepositoryImpl])

  def groups(): Seq[MonitorGroup] = {
    if (mongoEnabled) {
      database.monitorGroups.findAll(log)
    }
    else {
      log.debugElapsed {
        val groups = MonitorRouteView.groups(monitorAdminDatabase, stale = false)
        ("admin-groups", groups)
      }
    }
  }

  def group(groupName: String): Option[MonitorGroup] = {
    if (mongoEnabled) {
      database.monitorGroups.findByStringId(groupName, log)
    }
    else {
      log.debugElapsed {
        val groupOption = monitorAdminDatabase.docWithId(docId(groupName), classOf[MonitorGroupDoc]).map(_.monitorGroup)
        (s"admin-group $groupName", groupOption)
      }
    }
  }

  def saveGroup(routeGroup: MonitorGroup): Unit = {
    if (mongoEnabled) {
      database.monitorGroups.save(routeGroup, log)
    }
    else {
      log.debugElapsed {
        monitorAdminDatabase.save(MonitorGroupDoc(docId(routeGroup.name), routeGroup))
        (s"admin-save-group ${routeGroup.name}", ())
      }
    }
  }

  def deleteGroup(name: String): Unit = {
    if (mongoEnabled) {
      database.monitorGroups.deleteByStringId(name, log)
    }
    else {
      log.debugElapsed {
        monitorAdminDatabase.deleteDocWithId(docId(name))
        (s"admin-delete-group $name", ())
      }
    }
  }

  def groupRoutes(groupName: String): Seq[MonitorRoute] = {
    if (mongoEnabled) {
      database.monitorRoutes.find(
        filter(
          equal("groupName", groupName)
        ),
        log
      )
    }
    else {
      log.debugElapsed {
        val routes = MonitorRouteView.groupRoutes(monitorAdminDatabase, groupName, stale = false)
        (s"admin-group-routes $groupName", routes)
      }
    }
  }

  private def docId(name: String): String = {
    s"${KeyPrefix.MonitorGroup}:$name"
  }
}
