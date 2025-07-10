package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest20_osm_remove_relation_id extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("osm reference, remove relation id - delete obsolete reference and state") {

    val (group, route, reporter) = setup()

    executeMonitorUpdate(group, reporter)

    verifyReporterMessages(reporter)

    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(0)
    database.monitorRouteStates.countDocuments() should equal(0)

    verifyUpdatedRoute(group, route)

    configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
    configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)
  }

  private def executeMonitorUpdate(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route",
          referenceType = MonitorReferenceType.osm,
          description = Some("route description"),
          relationId = None,
          referenceTimestamp = Some(ReferenceTimestamp1),
        )
      )
    )
  }

  private def verifyUpdatedRoute(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        _id = route._id,
        groupId = group._id,
        name = "route",
        description = "route description",
        comment = None,
        relationId = None,
        user = "user",
        timestamp = UpdateTimestamp,
        symbol = None,
        analysisTimestamp = None,
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = None,
        referenceDistance = 0,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 0,
        osmDistance = 0,
        relation = None,
        happy = false
      )
    )
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

  private def setup() = {

    Time.set(CurrentTimestamp)

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    val route = setupRoute(group)
    val reference = setupReference(route)
    val state = setupState(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveRouteReference(reference)
    configuration.monitorRouteRepository.saveRouteState(state)

    Time.set(UpdateTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, route, reporter)
  }

  private def setupRoute(group: MonitorGroup): MonitorRoute = {
    newMonitorRoute(
      group._id,
      name = "route",
      relationId = Some(route1.relationId),
      user = "user",
      symbol = Some("red:red:white_bar"),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(ReferenceTimestamp1),
      referenceFilename = None,
      referenceDistance = 1000,
      deviationDistance = 100,
      deviationCount = 2,
      osmWayCount = 30,
      osmDistance = 1010,
      osmSegmentCount = 1,
      relation = None,
    )
  }

  private def setupReference(route: MonitorRoute): MonitorRouteReference = {
    newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(route1.relationId),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp1,
    )
  }

  private def setupState(route: MonitorRoute): MonitorRouteState = {
    newMonitorRouteState(
      routeId = route._id,
      relationId = route1.relationId,
      timestamp = CurrentTimestamp,
    )
  }
}
