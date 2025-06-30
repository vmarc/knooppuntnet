package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest14_update_group_error extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val ReferenceTimestamp = Timestamp(2022, 8, 1)
  private val CurrentTimestamp = Timestamp(2022, 8, 11, 12, 0, 0)
  private val UpdateTimestamp = Timestamp(2022, 8, 12, 12, 0, 0)

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route update - move to group that does not exist") {

    withDatabase() { database =>

      val (configuration, group1, route, state, reference, reporter) = setup(database)

      executeMonitorUpdate(configuration, reporter)

      database.monitorRoutes.countDocuments() should equal(1)
      database.monitorRouteReferences.countDocuments() should equal(1)
      database.monitorRouteStates.countDocuments() should equal(1)

      val updatedRoute = configuration.monitorRouteRepository.routeByName(group1._id, "route").get
      val updatedState = configuration.monitorRouteRepository.routeState(route._id, 1).get
      val updatedReference = configuration.monitorRouteRepository.routeReference(route._id, Some(1)).get

      updatedRoute should equal(route)
      updatedState should equal(state)
      updatedReference should equal(reference)

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
          groupName = "group1",
          newGroupName = Some("group2"), // <-- changed, but there is no group2
          routeName = "route",
          referenceType = MonitorReferenceType.osm,
          description = Some(""),
          comment = None,
          relationId = Some(1),
          referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        )
      )
    )
  }

  private def setup(database: Database) = {
    val configuration = MonitorUpdaterTestSupport.configuration(database)
    val group = newMonitorGroup("group1")
    val route = setupRoute(group)
    val reference = setupReference(route)
    val state = setupState(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveRouteReference(reference)
    configuration.monitorRouteRepository.saveRouteState(state)

    Time.set(UpdateTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (configuration, group, route, state, reference, reporter)
  }

  private def setupRoute(group: MonitorGroup) = {
    newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(1),
      timestamp = CurrentTimestamp,
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(ReferenceTimestamp),
      referenceFilename = None,
    )
  }

  private def setupReference(route: MonitorRoute) = {
    newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(1),
      timestamp = CurrentTimestamp,
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp,
    )
  }

  private def setupState(route: MonitorRoute) = {
    newMonitorRouteState(
      routeId = route._id,
      relationId = 1,
      timestamp = CurrentTimestamp,
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
          exception = Some("""Could not find group with name "group2"""")
        )
      )
    )
  }
}
