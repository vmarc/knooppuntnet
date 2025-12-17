package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteSegment
import kpn.core.test.TestObjects.newBaseRouteSegmentElement
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.test.TestObjects.newSuperSegment
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorSegment
import kpn.server.monitor.domain.MonitorState

class MonitorUpdaterTest18_update_gpx_to_osm extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("add route with gpx reference, and change to reference type osm afterwards") {

    val (group, reporter) = setup()

    executeAddRouteWithGpxReference(group, reporter)
    val addedRoute = verifyAdd(group)

    executeUpdateRouteWithOsmReference(group, reporter)
    verifyUpdate(group, addedRoute)
  }

  private def executeAddRouteWithGpxReference(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user1",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.gpx,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(route1.relationId),
          referenceTimestamp = Some(ReferenceTimestamp1),
          referenceFilename = Some("filename"),
          referenceGpx = Some(route1.gpx)
        )
      )
    )
  }

  private def executeUpdateRouteWithOsmReference(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    Time.set(UpdateTimestamp)

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user2",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.update,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.osmNow,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(route1.relationId),
        )
      )
    )
  }

  private def verifyAdd(group: MonitorGroup): MonitorRoute = {
    verifyDocumentCounts(1, 1, 1)
    val route = verifyAddedRoute(group)
    verifyAddedReference(route)
    val state = verifyAddedState(route)
    route
  }

  private def verifyUpdate(group: MonitorGroup, addedRoute: MonitorRoute): Unit = {
    verifyDocumentCounts(1, 1, 1)
    val updatedRoute = verifyUpdatedRoute(group, addedRoute)
    verifyUpdatedReference(addedRoute, updatedRoute)
    verifyUpdatedState(addedRoute, updatedRoute)
  }

  private def verifyAddedRoute(group: MonitorGroup) = {
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
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = Some("filename"),
        referenceDistance = route1.meters,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = route1.meters,
        relationIds = Seq(route1.relationId),
        bounds = Some(route1.bounds),
        happy = true
      )
    )
    route
  }

  private def verifyAddedReference(addedRoute: MonitorRoute) = {
    val reference = configuration.monitorRouteRepository.reference(addedRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorReference(
        reference._id,
        routeId = addedRoute._id,
        relationId = Some(route1.relationId),
        timestamp = CurrentTimestamp,
        user = "user1",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = route1.meters,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename"),
        referenceLines = route1.lines,
        tiles = route1.referenceTiles
      )
    )
    reference
  }

  private def verifyAddedState(addedRoute: MonitorRoute) = {
    val state = configuration.monitorRouteRepository.state(addedRoute._id, 1).get
    assertEqual(
      state,
      MonitorState(
        state._id,
        routeId = addedRoute._id,
        relationId = route1.relationId,
        timestamp = CurrentTimestamp,
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
        segments = Seq(MonitorSegment(1, 1, route1.lines.head))
      )
    )
    state
  }

  private def verifyUpdatedRoute(group: MonitorGroup, addedRoute: MonitorRoute) = {
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
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(UpdateTimestamp),
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
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = UpdateTimestamp,
        referenceDistance = route1.meters,
        referenceSegmentCount = 1,
        referenceFilename = None,
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
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
        segments = Seq.empty
      )
    )
    val stateTiles = configuration.monitorRouteRepository.stateTiles(addedRoute._id, route1.relationId)
    assertEqual(
      stateTiles.map(MonitorStateTileInfo.from),
      route1.stateTiles
    )
  }

  private def setup() = {

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
        route1.relationId,
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
        ),
        bounds = Some(route1.bounds),
      )
    )
  }

  private def setupLoadStructure(): Unit = {
    val monitorRouteRelation = route1.overpassStructure
    (monitorRouteStructureLoader.load _).returns { case (timestamp: Option[Timestamp], relationId: Long) =>
      Option.when(timestamp.contains(ReferenceTimestamp1) && relationId == route1.relationId) {
        monitorRouteRelation
      }
    }
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
    (monitorRouteRelationRepository.load _).returns { case (timestamp: Option[Timestamp], relationId: Long) =>
      Option.when(timestamp.isEmpty && relationId == route1.relationId) {
        relation
      }
    }
    (monitorRouteRelationRepository.loadTopLevel _).returns { case (timestamp: Option[Timestamp], relationId: Long) =>
      Option.when(timestamp.contains(UpdateTimestamp) && relationId == route1.relationId) {
        relation
      }
    }
  }

  private def setupRouteDoc(): Unit = {
    configuration.routeRepository.saveRoute(
      newRouteDoc(
        route1.relationId,
        base = newRouteBaseData(
          summary = newRouteSummary(
            name = "route-name"
          )
        ),
        superDistance = route1.meters,
        routeIds = Seq(route1.relationId),
        superSegments = Seq(
          newSuperSegment()
        ),
        bounds = Some(route1.bounds)
      )
    )
  }
}
