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
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest04_osm_add_super_route extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("add superroute osm reference") {

    val referenceTimestamp = Timestamp(2022, 8, 11)

    val (group, reporter) = setup(referenceTimestamp)

    executeMonitorUpdate(referenceTimestamp, group, reporter)

    verifyDocumentCounts()
    val route = verifyRoute(referenceTimestamp, group)
    verifyReference1(route)
    verifyReference11(route)
    verifyReference12(route)
    verifyState1(route)
    verifyState11(route)
    verifyState12(route)
  }

  private def executeMonitorUpdate(referenceTimestamp: Timestamp, group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(1),
          referenceTimestamp = Some(referenceTimestamp),
        )
      )
    )
  }

  private def verifyDocumentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(2)
    database.monitorRouteStates.countDocuments() should equal(2)
  }

  private def verifyRoute(referenceTimestamp: Timestamp, group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        route._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(1),
        user = "user",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(referenceTimestamp),
        referenceFilename = None,
        referenceDistance = 274,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 0,
        osmDistance = 0,
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
            name = "main-relation",
            happy = true,
            relations = Seq(
              newMonitorRouteRelation(
                relationId = 11,
                name = "sub-relation-1",
                happy = true,
              ),
              newMonitorRouteRelation(
                relationId = 12,
                name = "sub-relation-2",
                happy = true,
              )
            )
          )
        ),
        happy = true,
      )
    )
    route
  }

  private def verifyReference1(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(1))
    reference should equal(None)
  }

  private def verifyReference11(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(11)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(11),
        timestamp = CurrentTimestamp,
        user = "user",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Timestamp(2022, 8, 11),
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceGeoJson = Some("""{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}""")
      )
    )
  }

  private def verifyReference12(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(12)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(12),
        timestamp = CurrentTimestamp,
        user = "user",
        referenceBounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Timestamp(2022, 8, 11),
        referenceDistance = 93,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceGeoJson = Some("""{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}]}""")
      )
    )
  }

  private def verifyState1(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 1)
    state should equal(None)
  }

  private def verifyState11(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 11).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = 11,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesDistance = 181,
        matchesGeometry = Some(route1.multiLinestringGeoJson),
        deviations = Seq.empty,
      )
    )
  }

  private def verifyState12(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 12).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = 12,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
        matchesDistance = 181,
        matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4562458,51.4618272],[4.455056,51.4614496]]]}]}"""),
        deviations = Seq.empty,
      )
    )
  }

  private def setup(referenceTimestamp: Timestamp) = {

    setupLoadStructure()
    setupLoadTopLevel(referenceTimestamp)
    setupBaseRouteDoc1()
    setupBaseRouteDoc11()
    setupBaseRouteDoc12()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)

    val reporter = new MonitorUpdateReporterMock()
    (group, reporter)
  }

  private def setupBaseRouteDoc1(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(1),
        subRelationTree = Some(
          newRouteRelation(
            relationId = 1,
            name = "main-relation",
            relations = Seq(
              newRouteRelation(
                relationId = 11,
                name = "sub-relation-1",
              ),
              newRouteRelation(
                relationId = 12,
                name = "sub-relation-2",
              )
            )
          )
        )
      )
    )
  }

  private def setupBaseRouteDoc11(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(11),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = "[[4.4553911, 51.4633666],[4.4562458,51.4618272]]"
          )
        ),
      )
    )
  }

  private def setupBaseRouteDoc12(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(12),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = "[[4.4562458,51.4618272],[4.4550560,51.4614496]]"
          )
        ),
      )
    )
  }

  private def setupLoadStructure(): Unit = {

    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
        members = Seq(
          newMember(MemberType.Relation, 11),
          newMember(MemberType.Relation, 12)
        )
      )
      .relation(
        11,
        tags = Tags.from(
          "name" -> "sub-relation-1"
        ),
      )
      .relation(
        12,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
      )

    setupRouteStructure(Some(ReferenceTimestamp1), overpassData, 1)
  }

  private def setupLoadTopLevel(referenceTimestamp: Timestamp): Unit = {

    val overpassData = OverpassData()
      .node(1001, latitude = "51.4633666", longitude = "4.4553911")
      .node(1002, latitude = "51.4618272", longitude = "4.4562458")
      .node(1003, latitude = "51.4614496", longitude = "4.4550560")
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .relation(
        1,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
        members = Seq(
          newMember(MemberType.Relation, 11),
          newMember(MemberType.Relation, 12),
        )
      )
      .relation(
        11,
        tags = Tags.from(
          "name" -> "sub-relation-1"
        ),
        members = Seq(
          newMember(MemberType.Way, 101),
        )
      )
      .relation(
        12,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
        members = Seq(
          newMember(MemberType.Way, 102),
        )
      )

    val data = new DataBuilder(overpassData.rawData).data
    val mainRelation = data.relations(1)
    val subRelation1 = data.relations(11)
    val subRelation2 = data.relations(12)

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(referenceTimestamp), 1).returns(Some(mainRelation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(referenceTimestamp), 11).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(referenceTimestamp), 12).returns(Some(subRelation2))

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(mainRelation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 11).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 12).returns(Some(subRelation2))
  }
}
