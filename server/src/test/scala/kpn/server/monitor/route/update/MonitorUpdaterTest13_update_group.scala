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
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest13_update_group extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val ReferenceTimestamp = Timestamp(2022, 8, 1)
  private val CurrentTimestamp = Timestamp(2022, 8, 11, 12, 0, 0)

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route update - change group") {

    withDatabase() { database =>

      val (configuration, group1, group2, route, reference, state, reporter) = setup(database)

      executeMonitorUpdate(configuration, group1, group2, reporter)

      verifyDocumentCounts(database)
      verifyRoute(configuration, group2)
      verifyReference(configuration, route, reference)
      verifyState(configuration, route, state)
      verifyReporterMessages(reporter)
    }
  }

  private def executeMonitorUpdate(configuration: MonitorUpdaterConfiguration, group1: MonitorGroup, group2: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group1.name,
          newGroupName = Some(group2.name), // <-- changed
          routeName = "route",
          referenceType = MonitorReferenceType.osm,
          description = Some(""),
          relationId = Some(1),
          referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        )
      )
    )
  }

  private def verifyDocumentCounts(database: Database): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)
  }

  private def verifyRoute(configuration: MonitorUpdaterConfiguration, group2: MonitorGroup): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group2._id, "route").get
    route.groupId should equal(group2._id)
  }

  private def verifyReference(configuration: MonitorUpdaterConfiguration, route: MonitorRoute, reference: MonitorRouteReference): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(1)).get
    reference should equal(reference)
  }

  private def verifyState(configuration: MonitorUpdaterConfiguration, route: MonitorRoute, state: MonitorRouteState): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 1).get
    state should equal(state)
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
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "analyze-route-structure")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "save")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-done", "save")
          )
        )
      )
    )
  }

  private def setup(database: Database) = {
    val configuration = MonitorUpdaterTestSupport.configuration(database)

    val group1 = newMonitorGroup("group1")
    val group2 = newMonitorGroup("group2")
    val route = setupRoute(group1)
    val reference = setupReference(route)
    val state = setupState(route)

    configuration.monitorGroupRepository.saveGroup(group1)
    configuration.monitorGroupRepository.saveGroup(group2)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveRouteReference(reference)
    configuration.monitorRouteRepository.saveRouteState(state)

    Time.set(Timestamp(2023, 1, 1))
    val reporter = new MonitorUpdateReporterMock()
    (configuration, group1, group2, route, reference, state, reporter)
  }

  private def setupRoute(group: MonitorGroup) = {
    newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(1),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(Timestamp(2022, 8, 11)),
      referenceFilename = None,
    )
  }

  private def setupReference(route: MonitorRoute) = {
    newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(1),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Timestamp(2022, 8, 11),
    )
  }

  private def setupState(route: MonitorRoute) = {
    newMonitorRouteState(
      routeId = route._id,
      relationId = 1,
    )
  }
}
