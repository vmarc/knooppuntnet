package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database

class MonitorUpdaterTest17_route_not_found extends UnitTest with SharedTestObjects {

  private val ReferenceTimestamp = Timestamp(2022, 8, 1)
  private val CurrentTimestamp = Timestamp(2022, 8, 11, 12, 0, 0)

  test("update/upload - route not found") {

    withDatabase() { database =>

      val (configuration, reporter) = setup(database)

      executeMonitorUpdate(configuration, reporter)

      verifyReporterMessages(reporter)
    }
  }

  private def executeMonitorUpdate(configuration: MonitorUpdaterConfiguration, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = "group-name",
          routeName = "unknown-route-name",
          description = Some("description"),
          comment = Some("comment"),
          relationId = Some(1),
          referenceType = MonitorReferenceType.osm,
          referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        )
      )
    )
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-add", "prepare"),
            MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
            MonitorRouteUpdateStatusCommand("step-active", "prepare")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          exception = Some("""Could not find route with name "unknown-route-name" in group "group-name"""")
        )
      )
    )
  }

  private def setup(database: Database) = {
    val configuration = MonitorUpdaterTestSupport.configuration(database)

    val group = newMonitorGroup("group-name")
    configuration.monitorGroupRepository.saveGroup(group)

    val reporter = new MonitorUpdateReporterMock()
    (configuration, reporter)
  }
}
