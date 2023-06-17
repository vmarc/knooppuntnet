package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.ApiResponse
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

import scala.xml.Elem

@Component
class OldMonitorRouteUpdaterImpl(
  applicationContext: ApplicationContext
) extends OldMonitorRouteUpdater {

  private val log = Log(classOf[OldMonitorRouteUpdaterImpl])


  def update(
    user: String,
    update: MonitorRouteUpdate,
    reporter: MonitorUpdateReporter
  ): Unit = {
    Log.context(Seq("route-update", s"group=${update.groupName}", s"route=${update.routeName}")) {
      var context = MonitorUpdateContext(
        user,
        reporter,
        update,
      )
      applicationContext.getBean(classOf[MonitorRouteUpdateExecutor]).execute(context)
    }
  }

  override def add(
    user: String,
    groupName: String,
    properties: MonitorRouteProperties
  ): MonitorRouteSaveResult = {
    throw new RuntimeException("deprecated")
  }

  override def oldUpdate(
    user: String,
    groupName: String,
    routeName: String,
    properties: MonitorRouteProperties
  ): MonitorRouteSaveResult = {
    throw new RuntimeException("deprecated")
  }

  override def upload(
    user: String,
    groupName: String,
    routeName: String,
    relationId: Option[Long],
    referenceTimestamp: Timestamp,
    filename: String,
    xml: Elem
  ): MonitorRouteSaveResult = {
    throw new RuntimeException("deprecated")
  }

  override def resetSubRelationGpxReference(groupName: String, routeName: String, subRelationId: Long): Unit = {
    throw new RuntimeException("deprecated")
  }

  override def analyze(groupName: String, routeName: String): ApiResponse[MonitorRouteSaveResult] = {
    throw new RuntimeException("deprecated")
    null
  }

  override def analyzeAll(group: MonitorGroup, routeId: ObjectId): Unit = {
    throw new RuntimeException("deprecated")
  }

  override def analyzeRelation(group: MonitorGroup, routeId: ObjectId, relationId: Long): Unit = {
    throw new RuntimeException("deprecated")
  }
}
