package kpn.server.monitor.route.update

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatus
import kpn.api.common.monitor.MonitorRouteUpdateStep
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest13_add_error extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  test("cannot add route that already exists") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)

      val group = newMonitorGroup("group-name")
      val route = newMonitorRoute(
        group._id,
        name = "route-name",
        relationId = None,
        user = "user",
        referenceType = "osm",
        referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        referenceFilename = None,
      )

      configuration.monitorGroupRepository.saveGroup(group)
      configuration.monitorRouteRepository.saveRoute(route)

      val update = MonitorRouteUpdate(
        action = "add",
        groupName = "group-name",
        routeName = "route-name",
        referenceType = "osm",
        description = Some("description"),
        comment = Some("comment"),
        relationId = Some(1),
        referenceTimestamp = Some(Timestamp(2022, 8, 11)),
      )

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
            exception = Some(s"""Could not add route with name "route-name": already exists (_id=${route._id.oid}) in group with name "group-name"""")
          )
        )
      )
    }
  }
}
