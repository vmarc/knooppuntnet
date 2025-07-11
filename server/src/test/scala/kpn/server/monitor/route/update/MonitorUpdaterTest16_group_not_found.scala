package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp

class MonitorUpdaterTest16_group_not_found extends MonitorUpdateTest {

  test("add/update/upload - group not found") {

    val (reporter) = setup()

    executeMonitorUpdate(reporter)

    verifyDocumentCounts(0, 0, 0)
    verifyReporterMessages(reporter)
  }

  private def executeMonitorUpdate(reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = "unknown-group",
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          description = Some("description"),
          comment = Some("comment"),
          relationId = Some(1),
          referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        )
      )
    )
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        message(
          add("prepare"),
          add("analyze-route-structure"),
          active("prepare")
        ),
        MonitorRouteUpdateStatusMessage(
          exception = Some("""Could not find group with name "unknown-group"""")
        )
      )
    )
  }

  private def setup() = {
    val reporter = new MonitorUpdateReporterMock()
    (reporter)
  }
}
