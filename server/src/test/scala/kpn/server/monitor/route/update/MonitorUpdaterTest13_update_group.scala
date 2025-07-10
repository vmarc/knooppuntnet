package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest13_update_group extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("route update - change group") {

    val (group1, group2, route, reference, state, reporter) = setup()

    executeUpdate(group1, group2, reporter)

    verifyDocumentCounts()
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

  private def verifyDocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)
  }

  private def verifyRoute(group2: MonitorGroup): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group2._id, "route").get
    route.groupId should equal(group2._id)
  }

  private def verifyReference(route: MonitorRoute, reference: MonitorRouteReference): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(route1.relationId)).get
    reference should equal(reference)
  }

  private def verifyState(route: MonitorRoute, state: MonitorRouteState): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, route1.relationId).get
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
      MonitorRouteReference,
      MonitorRouteState,
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
    configuration.monitorRouteRepository.saveRouteReference(reference)
    configuration.monitorRouteRepository.saveRouteState(state)

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
    newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(route1.relationId),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp1,
    )
  }

  private def setupState(route: MonitorRoute) = {
    newMonitorRouteState(
      routeId = route._id,
      relationId = route1.relationId,
    )
  }
}
