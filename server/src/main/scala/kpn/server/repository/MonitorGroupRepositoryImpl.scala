package kpn.server.repository

import kpn.api.common.monitor.MonitorGroup
import kpn.core.database.Database
import kpn.core.database.views.monitor.MonitorRouteView
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import org.springframework.stereotype.Component

@Component
class MonitorGroupRepositoryImpl(monitorDatabase: Database) extends MonitorGroupRepository {

  private val log = Log(classOf[MonitorGroupRepositoryImpl])

  def groups(): Seq[MonitorGroup] = {
    MonitorRouteView.groups(monitorDatabase)
  }

  def groupRoutes(groupName: String): Seq[MonitorRoute] = {
    MonitorRouteView.groupRoutes(monitorDatabase, groupName)
  }

}
