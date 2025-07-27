package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest07_osm_update_properties extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("update name, description and comment (state and reference unchanged, no analysis)") {

    val (group, route, state, reference, reporter) = setup()

    executeUpdate(group, reporter)

    verifyDocumentCounts(1, 1, 1)
    verifyRoute(group, route)
    verifyReference(route, reference)
    verifyState(route, state)
    verifyReporterMessages(reporter)
  }

  private def executeUpdate(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user2",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          description = Some("description-changed"), // <-- changed
          comment = Some("comment-changed"), // <-- changed
          relationId = Some(route1.relationId),
          referenceTimestamp = Some(ReferenceTimestamp1),
          newRouteName = Some("route-name-changed") // <-- changed
        )
      )
    )
  }

  private def verifyRoute(group: MonitorGroup, route: MonitorRoute): Unit = {
    val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name-changed").get
    assertEqual(
      updatedRoute.copy(analysisTimestamp = None, analysisDuration = None),
      route.copy(
        name = "route-name-changed",
        description = "description-changed",
        comment = Some("comment-changed"),
        user = "user2",
        timestamp = UpdateTimestamp
      )
    )
  }

  private def verifyReference(route: MonitorRoute, reference: MonitorReference): Unit = {
    val updatedReference = configuration.monitorRouteRepository.reference(route._id, Some(1)).get
    updatedReference should equal(reference) // no change
  }

  private def verifyState(route: MonitorRoute, state: MonitorState): Unit = {
    val updatedState = configuration.monitorRouteRepository.state(route._id, 1).get
    updatedState should equal(state) // no change
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    // not analyzed, no errors
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

    val group = newMonitorGroup("group")
    val route = setupRoute(group)
    val state = setupState(route)
    val reference = setupReference(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveState(state)
    configuration.monitorRouteRepository.saveReference(reference)

    Time.set(UpdateTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, route, state, reference, reporter)
  }

  private def setupRoute(group: MonitorGroup) = {
    MonitorRoute(
      ObjectId(),
      groupId = group._id,
      name = "route-name",
      description = "route-description",
      comment = Some("route-comment"),
      relationId = Some(route1.relationId),
      user = "user1",
      timestamp = CurrentTimestamp,
      symbol = None,
      analysisTimestamp = None,
      analysisDuration = None,
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(ReferenceTimestamp1),
      referenceFilename = None,
      referenceDistance = route1.meters,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = 1,
      osmDistance = route1.meters,
      relationIds = Seq(route1.relationId),
      bounds = Some(route1.bounds),
      happy = true
    )
  }

  private def setupState(route: MonitorRoute) = {
    MonitorState(
      ObjectId(),
      routeId = route._id,
      relationId = route1.relationId,
      timestamp = CurrentTimestamp,
      // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
      deviations = Seq.empty,
      matchesDistance = route1.meters,
      matchesLines = route1.lines,
      segments = Seq.empty
    )
  }

  private def setupReference(route: MonitorRoute) = {
    MonitorReference(
      ObjectId(),
      routeId = route._id,
      relationId = Some(route1.relationId),
      timestamp = CurrentTimestamp,
      user = "user1",
      referenceBounds = route1.bounds,
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = ReferenceTimestamp1,
      referenceDistance = route1.meters,
      referenceSegmentCount = 1,
      referenceFilename = None,
      referenceLines = route1.lines,
      tiles = route1.referenceTiles
    )
  }
}
