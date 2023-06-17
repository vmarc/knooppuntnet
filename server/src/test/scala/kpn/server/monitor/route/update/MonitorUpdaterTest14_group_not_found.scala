package kpn.server.monitor.route.update

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatus
import kpn.api.common.monitor.MonitorRouteUpdateStep
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest14_group_not_found extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  test("add/update/upload - group not found") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)

      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user",
          reporter,
          MonitorRouteUpdate(
            action = "add",
            groupName = "unknown-group",
            routeName = "route-name",
            referenceType = "osm",
            description = Some("description"),
            comment = Some("comment"),
            relationId = Some(1),
            referenceTimestamp = Some(Timestamp(2022, 8, 11)),
          )
        )
      )

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
            exception = Some("""Could not find group with name "unknown-group"""")
          )
        )
      )
    }
  }
}
