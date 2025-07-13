package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.core.doc.SuperSegment
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteSegment
import kpn.core.test.TestObjects.newBaseRouteSegmentElement
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest09_gpx_add_without_relation_id extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("add non-super route with single gpx reference, but initially with relationId unknown") {

    val (group, reporter) = setup()

    val update = executeAddWithoutRelationId(group, reporter)
    val (route, reference) = verifyAdd(group, reporter)

    executeUpdateWithRelationId(update)
    verifyUpdate(group, reporter, route, reference)
  }

  private def verifyAdd(group: MonitorGroup, reporter: MonitorUpdateReporterMock) = {
    verifyDocumentCounts(1, 1, 0)
    val route = verifyAdd_route(group)
    verifyAdd_noReference(route)
    verifyAdd_noState(route)
    val reference = verifyAdd_reference(route)
    verifyAdd_reporterMessages(reporter)
    (route, reference)
  }

  private def verifyUpdate(group: MonitorGroup, reporter: MonitorUpdateReporterMock, route: MonitorRoute, reference: MonitorReference): Unit = {
    verifyDocumentCounts(1, 1, 1)
    verifyUpdate_route(group, route)
    verifyUpdate_reference(route, reference)
    verifyUpdate_state(route)
    verifyUpdate_reporterMessages(reporter)
  }

  private def executeAddWithoutRelationId(group: MonitorGroup, reporter: MonitorUpdateReporterMock): MonitorRouteUpdate = {

    val update = MonitorRouteUpdate(
      action = MonitorAction.add,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.gpx,
      description = Some("route-description"),
      comment = Some("route-comment"),
      relationId = None, // <-- no relationId yet
      referenceTimestamp = Some(ReferenceTimestamp1),
      referenceFilename = Some("filename"),
      referenceGpx = Some(route1.gpx)
    )

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user1",
        reporter,
        update
      )
    )
    update
  }

  private def executeUpdateWithRelationId(update: MonitorRouteUpdate): Unit = {

    Time.set(UpdateTimestamp)

    val reporter = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user2",
        reporter,
        update.copy(
          action = MonitorAction.update,
          relationId = Some(route1.relationId),
          referenceGpx = None
        )
      )
    )
  }

  private def verifyAdd_noState(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.state(route._id, route1.relationId) should equal(None)
  }

  private def verifyAdd_noReference(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.reference(route._id, Some(route1.relationId)) should equal(None)
  }

  private def verifyAdd_route(group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        route._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = None, // <-- no relationId yet
        user = "user1",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = Some("filename"),
        referenceDistance = route1.meters,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 0,
        osmDistance = 0,
        happy = false,
        relation = None
      )
    )
    route
  }

  private def verifyAdd_reference(route: MonitorRoute): MonitorReference = {
    val reference = configuration.monitorRouteRepository.reference(route._id, None).get
    assertEqual(
      reference,
      MonitorReference(
        reference._id,
        routeId = route._id,
        relationId = None, // <-- not filled in
        timestamp = CurrentTimestamp,
        user = "user1",
        referenceBounds = route1.bounds,
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = route1.meters,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename"),
        referenceLines = route1.lines
      )
    )
    reference
  }

  private def verifyUpdate_route(group: MonitorGroup, route: MonitorRoute): Unit = {
    val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      updatedRoute.copy(analysisDuration = None),
      route.copy(
        relationId = Some(route1.relationId),
        user = "user2",
        timestamp = UpdateTimestamp,
        analysisTimestamp = Some(UpdateTimestamp),
        analysisDuration = None,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = Some("filename"),
        referenceDistance = route1.meters,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = route1.meters,
        relation = None,
        happy = true
      )
    )
  }

  private def verifyUpdate_reference(route: MonitorRoute, reference: MonitorReference): Unit = {
    val updatedReference = configuration.monitorRouteRepository.reference(route._id, Some(route1.relationId)).get
    assertEqual(
      updatedReference,
      MonitorReference(
        reference._id,
        routeId = route._id,
        relationId = Some(route1.relationId), // <-- filled in
        timestamp = CurrentTimestamp, // <-- date that reference was added, not latest change by "user2"
        user = "user1", // <-- not "user2" who provided the relationId, the reference was still added by "user1"
        referenceBounds = route1.bounds,
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = route1.meters,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename"),
        referenceLines = route1.lines
      )
    )
  }

  private def verifyUpdate_state(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.state(route._id, 1).get
    assertEqual(
      state,
      MonitorState(
        state._id,
        routeId = route._id,
        relationId = route1.relationId,
        timestamp = UpdateTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
      )
    )
  }

  private def verifyAdd_reporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        message(
          add("prepare"),
          add("analyze-route-structure"),
          active("prepare")
        ),
        message(
          active("analyze-route-structure")
        ),
        message(
          active("save")
        ),
        message(
          done("save")
        )
      )
    )
  }

  private def verifyUpdate_reporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        message(
          add("prepare"),
          add("analyze-route-structure"),
          active("prepare")
        ),
        message(
          active("analyze-route-structure")
        ),
        message(
          active("save")
        ),
        message(
          done("save")
        )
      )
    )
  }

  private def setup() = {

    setupBaseRouteDoc()
    setupRouteDoc()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, reporter)
  }

  private def setupBaseRouteDoc(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(route1.relationId),
        segments = Seq(
          newBaseRouteSegment(route1.relationId)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = route1.coordinateString
          )
        )
      )
    )
  }

  private def setupRouteDoc(): Unit = {
    configuration.routeRepository.saveRoute(
      newRouteDoc(
        newRouteSummary(
          route1.relationId,
          name = "route-name"
        ),
        superDistance = route1.meters,
        routeIds = Seq(route1.relationId),
        superSegments = Seq(
          SuperSegment(
            Seq.empty
          )
        )
      )
    )
  }
}
