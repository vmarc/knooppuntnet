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
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest16_group_not_found extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val ReferenceTimestamp = Timestamp(2022, 8, 1)
  private val CurrentTimestamp = Timestamp(2022, 8, 11, 12, 0, 0)

  test("add/update/upload - group not found") {

    withDatabase() { database =>

      val (configuration, reporter) = setup(database)

      executeMonitorUpdate(configuration, reporter)

      verifyDocumentCounts(database)
      verifyReporterMessages(reporter)
    }
  }

  private def executeMonitorUpdate(configuration: MonitorUpdaterConfiguration, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
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

  private def verifyDocumentCounts(database: Database) = {
    database.monitorRoutes.countDocuments() should equal(0)
    database.monitorRouteReferences.countDocuments() should equal(0)
    database.monitorRouteStates.countDocuments() should equal(0)
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
          exception = Some("""Could not find group with name "unknown-group"""")
        )
      )
    )
  }

  private def setup(database: Database) = {
    val configuration = MonitorUpdaterTestSupport.configuration(database)
    val reporter = new MonitorUpdateReporterMock()
    (configuration, reporter)
  }
}
