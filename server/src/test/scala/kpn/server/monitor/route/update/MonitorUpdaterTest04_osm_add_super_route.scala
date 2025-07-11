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
import kpn.core.test.TestObjects.newRouteRelation
import kpn.core.test.TestObjects.newRouteSummary
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest04_osm_add_super_route extends MonitorUpdateTest {

  private var subRoute11: MonitorTestRoute = _
  private var subRoute12: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    subRoute11 = TestSuperRoute.subRoute11
    subRoute12 = TestSuperRoute.subRoute12
  }

  test("add superroute osm reference") {

    val (group, reporter) = setup()

    executeAdd(group, reporter)

    verifyDocumentCounts(1, 2, 2)
    val route = verifyRoute(group)
    verifyReference1(route)
    verifyReference11(route)
    verifyReference12(route)
    verifyState1(route)
    verifyState11(route)
    verifyState12(route)
  }

  private def executeAdd(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.add,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.osm,
          description = Some("route-description"),
          comment = Some("route-comment"),
          relationId = Some(TestSuperRoute.MainRelationId),
          referenceTimestamp = Some(ReferenceTimestamp1),
        )
      )
    )
  }

  private def verifyRoute(group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        route._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(TestSuperRoute.MainRelationId),
        user = "user",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = None,
        referenceDistance = subRoute11.meters + subRoute12.meters,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = subRoute11.meters + subRoute12.meters,
        relation = None,
        happy = true,
      )
    )
    route
  }

  private def verifyReference1(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(TestSuperRoute.MainRelationId))
    reference should equal(None)
  }

  private def verifyReference11(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(subRoute11.relationId)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(subRoute11.relationId),
        timestamp = CurrentTimestamp,
        user = "user",
        referenceBounds = subRoute11.bounds,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = subRoute11.meters,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceLines = subRoute11.lines,
      )
    )
  }

  private def verifyReference12(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(subRoute12.relationId)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(subRoute12.relationId),
        timestamp = CurrentTimestamp,
        user = "user",
        referenceBounds = subRoute12.bounds,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = ReferenceTimestamp1,
        referenceDistance = subRoute12.meters,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceLines = subRoute12.lines
      )
    )
  }

  private def verifyState1(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, TestSuperRoute.MainRelationId)
    state should equal(None)
  }

  private def verifyState11(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, subRoute11.relationId).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = subRoute11.relationId,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = subRoute11.meters,
        matchesLines = subRoute11.lines,
      )
    )
  }

  private def verifyState12(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, subRoute12.relationId).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = subRoute12.relationId,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = subRoute12.meters,
        matchesLines = subRoute12.lines,
      )
    )
  }

  private def setup(): (MonitorGroup, MonitorUpdateReporterMock) = {

    setupLoadStructure()
    setupLoadTopLevel()
    setupBaseRouteDoc1()
    setupBaseRouteDoc11()
    setupBaseRouteDoc12()
    setupRouteDoc()

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)

    val reporter = new MonitorUpdateReporterMock()
    (group, reporter)
  }

  private def setupBaseRouteDoc1(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(TestSuperRoute.MainRelationId),
        subRelationTree = Some(
          newRouteRelation(
            relationId = TestSuperRoute.MainRelationId,
            name = "main-relation",
            relations = Seq(
              newRouteRelation(
                relationId = subRoute11.relationId,
                name = "sub-relation-1",
              ),
              newRouteRelation(
                relationId = subRoute12.relationId,
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
        newRouteSummary(subRoute11.relationId),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = subRoute11.lines.head
          )
        ),
      )
    )
  }

  private def setupBaseRouteDoc12(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(subRoute12.relationId),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = subRoute12.lines.head
          )
        ),
      )
    )
  }

  private def setupRouteDoc(): Unit = {
    configuration.routeRepository.saveRoute(
      newRouteDoc(
        newRouteSummary(
          TestSuperRoute.MainRelationId,
          name = "route-name"
        ),
        superDistance = subRoute11.meters + subRoute12.meters,
        routeIds = Seq(TestSuperRoute.MainRelationId, subRoute11.relationId, subRoute12.relationId),
        superSegments = Seq(
          SuperSegment(
            Seq.empty
          )
        )
      )
    )
  }

  private def setupLoadStructure(): Unit = {

    val overpassData = OverpassData()
      .relation(
        TestSuperRoute.MainRelationId,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
        members = Seq(
          newMember(MemberType.Relation, subRoute11.relationId),
          newMember(MemberType.Relation, subRoute12.relationId)
        )
      )
      .relation(
        subRoute11.relationId,
        tags = Tags.from(
          "name" -> "sub-relation-1"
        ),
      )
      .relation(
        subRoute12.relationId,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
      )

    setupRouteStructure(Some(ReferenceTimestamp1), overpassData, TestSuperRoute.MainRelationId)
  }

  private def setupLoadTopLevel(): Unit = {

    val overpassData = OverpassData()
      .node(1001, latitude = subRoute11.lat1, longitude = subRoute11.lon1)
      .node(1002, latitude = subRoute11.lat2, longitude = subRoute11.lon2)
      .node(1003, latitude = subRoute12.lat2, longitude = subRoute12.lon2)
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .relation(
        1,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
        members = Seq(
          newMember(MemberType.Relation, subRoute11.relationId),
          newMember(MemberType.Relation, subRoute12.relationId),
        )
      )
      .relation(
        subRoute11.relationId,
        tags = Tags.from(
          "name" -> "sub-relation-1"
        ),
        members = Seq(
          newMember(MemberType.Way, 101),
        )
      )
      .relation(
        subRoute12.relationId,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
        members = Seq(
          newMember(MemberType.Way, 102),
        )
      )

    val data = new DataBuilder(overpassData.rawData).data
    val mainRelation = data.relations(TestSuperRoute.MainRelationId)
    val subRelation1 = data.relations(subRoute11.relationId)
    val subRelation2 = data.relations(subRoute12.relationId)

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp1), TestSuperRoute.MainRelationId).returns(Some(mainRelation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp1), subRoute11.relationId).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp1), subRoute12.relationId).returns(Some(subRelation2))

    //    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(mainRelation))
    //    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 11).returns(Some(subRelation1))
    //    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 12).returns(Some(subRelation2))
  }
}
