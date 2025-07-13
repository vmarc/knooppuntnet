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

class MonitorUpdaterTest20_osm_remove_relation_id extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("osm reference, remove relation id - delete obsolete reference and state") {

    val (group, route, reporter) = setup()

    executeMonitorUpdate(group, reporter)

    verifyDocumentCounts(1, 0, 0)
    verifyUpdatedRoute(group, route)
    verifyReporterMessages(reporter)
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
    configuration.monitorRouteRepository.saveReference(reference)
    configuration.monitorRouteRepository.saveState(state)

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
      osmDistance = 1010,
      osmSegmentCount = 1
    )
  }

  private def setupReference(route: MonitorRoute): MonitorReference = {
    newMonitorReference(
      routeId = route._id,
      relationId = Some(route1.relationId),
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp1,
    )
  }

  private def setupState(route: MonitorRoute): MonitorState = {
    newMonitorState(
      routeId = route._id,
      relationId = route1.relationId,
      timestamp = CurrentTimestamp,
    )
  }
}
