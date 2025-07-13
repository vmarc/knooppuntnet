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
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest19_update_osm_to_gpx extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("add route with osm reference, and change to reference type gpx afterwards") {

    val (group, reporter) = setup()

    executeAdd(group, reporter)
    val addedRoute = verifyAdd(group)

    executeUpdate(group, reporter)
    verifyUpdate(group, addedRoute)
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
          referenceType = MonitorReferenceType.gpx,
          referenceTimestamp = Some(ReferenceTimestamp1),
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(route1.relationId),
          referenceFilename = Some("filename"),
          referenceGpx = Some(route1.gpx)
        )
      )
    )
  }

  private def verifyAdd(group: MonitorGroup) = {
    verifyDocumentCounts(1, 1, 1)

    val addedRoute = verifyAddedRoute(group)
    verifyAddedReference(addedRoute)
    verifyAddedState(addedRoute)
    addedRoute
  }

  private def verifyUpdate(group: MonitorGroup, addedRoute: MonitorRoute): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorReferences.countDocuments() should equal(1)
    database.monitorStates.countDocuments() should equal(1)

    val updatedRoute = verifyUpdatedRoute(group, addedRoute)
    verifyUpdatedReference(addedRoute, updatedRoute)
    verifyUpdatedState(addedRoute, updatedRoute)
  }

  private def verifyAddedRoute(group: MonitorGroup): MonitorRoute = {
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
        happy = true
      )
    )
    route
  }

  private def verifyAddedReference(addedRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.reference(addedRoute._id, Some(route1.relationId)).get
    assertEqual(
      reference,
      MonitorReference(
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
        referenceLines = route1.lines,
        tiles = route1.referenceTiles
      )
    )
  }

  private def verifyAddedState(addedRoute: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.state(addedRoute._id, route1.relationId).get
    assertEqual(
      state,
      MonitorState(
        state._id,
        routeId = addedRoute._id,
        relationId = route1.relationId,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
        tiles = route1.stateTiles
      )
    )
  }

  private def verifyUpdatedRoute(group: MonitorGroup, addedRoute: MonitorRoute): MonitorRoute = {
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
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = Some("filename"),
        referenceDistance = route1.meters,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = route1.meters,
        happy = true
      )
    )
    route
  }

  private def verifyUpdatedReference(addedRoute: MonitorRoute, updatedRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.reference(updatedRoute._id, Some(route1.relationId)).get
    assertEqual(
      reference,
      MonitorReference(
        reference._id,
        routeId = addedRoute._id,
        relationId = Some(route1.relationId),
        timestamp = UpdateTimestamp,
        user = "user2",
        referenceBounds = route1.bounds,
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = route1.meters,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename"),
        referenceLines = route1.lines,
        tiles = route1.referenceTiles
      )
    )
  }

  private def verifyUpdatedState(addedRoute: MonitorRoute, updatedRoute: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.state(updatedRoute._id, route1.relationId).get
    assertEqual(
      state,
      MonitorState(
        state._id,
        routeId = addedRoute._id,
        relationId = route1.relationId,
        timestamp = UpdateTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
        tiles = route1.stateTiles
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
            meters = route1.meters,
            coordinates = route1.coordinateString
          )
        )
      )
    )
  }

  private def setupLoadStructure(): Unit = {
    val monitorRouteRelation = route1.overpassStructure
    (configuration.monitorRouteStructureLoader.load _).when(Some(ReferenceTimestamp1), route1.relationId).returns(Some(monitorRouteRelation))
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
