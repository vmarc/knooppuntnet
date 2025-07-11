package kpn.server.monitor.route.update

import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Tags
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.doc.SuperSegment
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteSegment
import kpn.core.test.TestObjects.newBaseRouteSegmentElement
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest19_update_osm_to_multi_gpx extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("add route with osm reference, and change to reference type multi-gpx afterwards") {

    val (group, reporter) = setup()

    executeAdd(group, reporter)
    val (addedRoute, addedState) = verifyAdd(group)

    executeUpdate(group, reporter)
    verifyUpdate(group, addedRoute, addedState)
  }

  private def executeAdd(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user1",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(route1.relationId),
          referenceTimestamp = Some(ReferenceTimestamp1),
        )
      )
    )
  }

  private def executeUpdate(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    Time.set(UpdateTimestamp)
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user2",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.multiGpx,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(route1.relationId),
        )
      )
    )
  }

  private def verifyAdd(group: MonitorGroup): (MonitorRoute, MonitorRouteState) = {
    verifyDocumentCounts(1, 1, 1)
    val addedRoute = assertAddedRoute(group)
    assertAddedReference(addedRoute)
    val addedState = assertAddedState(addedRoute)
    (addedRoute, addedState)
  }

  private def verifyUpdate(group: MonitorGroup, addedRoute: MonitorRoute, addedState: MonitorRouteState): Unit = {
    verifyDocumentCounts(1, 0, 0)
    verifyUpdatedRoute(group, addedRoute)
  }

  private def assertAddedRoute(group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        route._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(route1.relationId),
        user = "user1",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = None,
        referenceDistance = route1.meters,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = route1.meters,
        relation = None,
        happy = true
      )
    )
    route
  }

  private def assertAddedReference(addedRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(addedRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = addedRoute._id,
        relationId = Some(route1.relationId),
        timestamp = CurrentTimestamp,
        user = "user1",
        referenceBounds = route1.bounds,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = route1.meters,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceLines = route1.lines
      )
    )
  }

  private def assertAddedState(addedRoute: MonitorRoute): MonitorRouteState = {
    val state = configuration.monitorRouteRepository.routeState(addedRoute._id, route1.relationId).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = addedRoute._id,
        relationId = route1.relationId,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
      )
    )
    state
  }

  private def verifyUpdatedRoute(group: MonitorGroup, addedRoute: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        addedRoute._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(route1.relationId),
        user = "user2",
        timestamp = UpdateTimestamp,
        symbol = None,
        analysisTimestamp = Some(UpdateTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.multiGpx,
        referenceTimestamp = None,
        referenceFilename = None,
        referenceDistance = 0,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = route1.meters,
        relation = None,
        happy = false
      )
    )
  }

  private def assertUpdatedState(addedRoute: MonitorRoute, addedState: MonitorRouteState, updatedRoute: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(updatedRoute._id, 1).get
    assertEqual(
      state,
      MonitorRouteState(
        addedState._id,
        routeId = addedRoute._id,
        relationId = route1.relationId,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
      )
    )
  }

  private def setup(): (MonitorGroup, MonitorUpdateReporterMock) = {

    setupLoadStructure()
    setupLoadRelation()
    setupBaseRouteDoc()
    setupRouteDoc()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, reporter)
  }

  private def setupLoadStructure(): Unit = {
    val overpassData = OverpassData()
      .relation(
        route1.relationId,
        tags = Tags.from(
          "name" -> "route-name"
        )
      )
    setupRouteStructure(Some(ReferenceTimestamp1), overpassData, 1)
  }

  private def setupLoadRelation(): Unit = {

    val overpassData = OverpassData()
      .node(1001, latitude = route1.lat1, longitude = route1.lon1)
      .node(1002, latitude = route1.lat2, longitude = route1.lon2)
      .way(101, 1001, 1002)
      .relation(
        route1.relationId,
        tags = Tags.from(
          "name" -> "route-name"
        ),
        members = Seq(
          newMember(MemberType.Way, 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(route1.relationId)
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp1), route1.relationId).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.load _).when(Some(ReferenceTimestamp1), route1.relationId).returns(Some(relation))
  }

  private def setupBaseRouteDoc(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(route1.relationId),
        segments = Seq(
          newBaseRouteSegment(1)
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
