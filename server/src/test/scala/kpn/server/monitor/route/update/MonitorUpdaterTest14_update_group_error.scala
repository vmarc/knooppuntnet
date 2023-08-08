package kpn.server.monitor.route.update

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest14_update_group_error extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val log = new MockLog()

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route update - move to group that does not exist") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)

      val group1 = newMonitorGroup("group1")

      val route = newMonitorRoute(
        group1._id,
        name = "route",
        relationId = Some(1),
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        referenceType = "osm",
        referenceTimestamp = Some(Timestamp(2022, 8, 1)),
        referenceFilename = None,
      )
      val state = newMonitorRouteState(
        routeId = route._id,
        relationId = 1,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
      )
      val reference = newMonitorRouteReference(
        routeId = route._id,
        relationId = Some(1),
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        referenceType = "osm",
        referenceTimestamp = Timestamp(2022, 8, 1),
      )

      configuration.monitorGroupRepository.saveGroup(group1)
      configuration.monitorRouteRepository.saveRoute(route)
      configuration.monitorRouteRepository.saveRouteState(state)
      configuration.monitorRouteRepository.saveRouteReference(reference)

      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))
      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user",
          reporter,
          MonitorRouteUpdate(
            action = "update",
            groupName = "group1",
            newGroupName = Some("group2"), // <-- changed, but there is no group2
            routeName = "route",
            referenceType = "osm",
            description = Some(""),
            comment = None,
            relationId = Some(1),
            referenceTimestamp = Some(Timestamp(2022, 8, 11)),
          )
        )
      )
      reporter.messages.shouldMatchTo(
        Seq(
          MonitorRouteUpdateStatusMessage(
            commands = Seq(
              MonitorRouteUpdateStatusCommand("step-add", "prepare"),
              MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
              MonitorRouteUpdateStatusCommand("step-active", "prepare")
            )
          ),
          MonitorRouteUpdateStatusMessage(
            exception = Some("""Could not find group with name "group2"""")
          )
        )
      )

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(1)

      val updatedRoute = configuration.monitorRouteRepository.routeByName(group1._id, "route").get
      val updatedState = configuration.monitorRouteRepository.routeState(route._id, 1).get
      val updatedReference = configuration.monitorRouteRepository.routeReference(route._id, Some(1)).get

      updatedRoute should equal(route)
      updatedState should equal(state)
      updatedReference should equal(reference)
    }
  }
}
