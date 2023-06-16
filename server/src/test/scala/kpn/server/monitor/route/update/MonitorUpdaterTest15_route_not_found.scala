package kpn.server.monitor.route.update

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatus
import kpn.api.common.monitor.MonitorRouteUpdateStep
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MonitorUpdaterTest15_route_not_found extends UnitTest with SharedTestObjects {

  test("update/upload - route not found") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)

      val update = MonitorRouteUpdate(
        action = "update",
        groupName = "group-name",
        routeName = "unknown-route-name",
        description = Some("description"),
        comment = Some("comment"),
        relationId = Some(1),
        referenceType = "osm",
        referenceTimestamp = Some(Timestamp(2022, 8, 11)),
      )

      val group = newMonitorGroup("group-name")
      configuration.monitorGroupRepository.saveGroup(group)

      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorUpdater.update("user", update, reporter)

      reporter.statusses.shouldMatchTo(
        Seq(
          MonitorRouteUpdateStatus(
            steps = Seq(
              MonitorRouteUpdateStep("definition", "busy")
            ),
          ),
          MonitorRouteUpdateStatus(
            steps = Seq(
              MonitorRouteUpdateStep("definition", "busy")
            ),
            exception = Some("""Could not find route with name "unknown-route-name" in group "group-name"""")
          )
        )
      )
    }
  }
}
