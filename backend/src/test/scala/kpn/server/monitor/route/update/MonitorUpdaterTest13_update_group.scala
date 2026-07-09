package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newMonitorReference
import kpn.core.test.TestObjects.newMonitorRoute
import kpn.core.test.TestObjects.newMonitorState
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest13_update_group extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("route update - change group") {

    val (group1, group2, route, reference, state, reporter) = setup()

    executeUpdate(group1, group2, reporter)

    verifyDocumentCounts(1, 1, 1)
    verifyRoute(group2)
    verifyReference(route, reference)
    verifyState(route, state)
    verifyReporterMessages(reporter)
  }

  private def executeUpdate(group1: MonitorGroup, group2: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group1.name,
          newGroupName = Some(group2.name), // <-- changed
          routeName = "route",
          referenceType = MonitorReferenceType.osm,
          description = Some(""),
          relationId = Some(route1.relationId),
          referenceTimestamp = Some(ReferenceTimestamp1),
        )
      )
    )
  }

  private def verifyRoute(group2: MonitorGroup): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group2._id, "route").get
    route.groupId should equal(group2._id)
  }

  private def verifyReference(route: MonitorRoute, reference: MonitorReference): Unit = {
    val reference = configuration.monitorRouteRepository.reference(route._id, Some(route1.relationId)).get
    reference should equal(reference)
  }

  private def verifyState(route: MonitorRoute, state: MonitorState): Unit = {
    val state = configuration.monitorRouteRepository.state(route._id, route1.relationId).get
    state should equal(state)
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        message(
          add("prepare"),
          active("prepare")
        ),
        message(
          add("save"),
          active("save")
        ),
        message(
          done("save")
        )
      )
    )
  }

  private def setup(): (
    MonitorGroup,
      MonitorGroup,
      MonitorRoute,
      MonitorReference,
      MonitorState,
      MonitorUpdateReporterMock
    ) = {

    val group1 = newMonitorGroup("group1")
    val group2 = newMonitorGroup("group2")
    val route = setupRoute(group1)
    val reference = setupReference(route)
    val state = setupState(route)

    configuration.monitorGroupRepository.saveGroup(group1)
    configuration.monitorGroupRepository.saveGroup(group2)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveReference(reference)
    configuration.monitorRouteRepository.saveState(state)

    Time.set(CurrentTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group1, group2, route, reference, state, reporter)
  }

  private def setupRoute(group: MonitorGroup) = {
    newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(route1.relationId),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(ReferenceTimestamp1),
      referenceFilename = None,
    )
  }

  private def setupReference(route: MonitorRoute) = {
    newMonitorReference(
      routeId = route._id,
      relationId = Some(route1.relationId),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp1,
    )
  }

  private def setupState(route: MonitorRoute) = {
    newMonitorState(
      routeId = route._id,
      relationId = route1.relationId,
    )
  }
}
