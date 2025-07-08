package kpn.server.monitor.route.update

import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Tags
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest03_osm_add_without_relation_id extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("add route with osm reference, initially with relationId unknown") {

    val (group, reporter) = setup()

    val update = executeAddWithoutRelationId(group, reporter)
    val route = verifyAdd(group)

    executeUpdateWithRelationId(reporter, update)
    verifyUpdate(group, route)
  }

  private def executeAddWithoutRelationId(group: MonitorGroup, reporter: MonitorUpdateReporterMock): MonitorRouteUpdate = {
    val update = MonitorRouteUpdate(
      action = MonitorAction.add,
      groupName = group.name,
      routeName = "route-name",
      referenceType = MonitorReferenceType.osm,
      description = Some(""),
      referenceTimestamp = Some(ReferenceTimestamp1),
    )

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        update
      )
    )
    update
  }

  private def executeUpdateWithRelationId(reporter: MonitorUpdateReporterMock, update: MonitorRouteUpdate): Unit = {
    setupLoadStructure()
    setupLoadTopLevel()
    setupBaseRouteDoc()

    val updatedUpdate = update.copy(
      action = MonitorAction.update,
      relationId = Some(route1.relationId)
    )

    Time.set(UpdateTimestamp)

    val reporter2 = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        updatedUpdate
      )
    )
  }

  private def verifyAdd(group: MonitorGroup) = {
    verifyAdd_documentCounts()
    val route = verifyAdd_route(group)
    verifyAdd_noReference(route)
    verifyAdd_noState(route)
    route
  }

  private def verifyAdd_documentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(0)
    database.monitorRouteStates.countDocuments() should equal(0)
  }

  private def verifyAdd_route(group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        _id = route._id,
        groupId = group._id,
        name = "route-name",
        description = "",
        comment = None,
        relationId = None, // no relationId yet
        user = "user",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = None,
        referenceDistance = 0,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 0,
        osmDistance = route1.meters,
        happy = false,
        relation = None // route structure not known yet
      )
    )
    route
  }

  private def verifyAdd_noReference(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.routeReference(route._id, Some(route1.relationId)) should equal(None)
  }

  private def verifyAdd_noState(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.routeState(route._id, route1.relationId) should equal(None)
  }

  private def verifyUpdate(group: MonitorGroup, route: MonitorRoute): Unit = {
    verifyUpdate_documentCounts()
    verifyUpdate_route(group, route)
    verifyUpdate_reference(route)
    verifyUpdate_state(route)
  }

  private def verifyUpdate_documentCounts(): Unit = {
    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(1)
    database.monitorRouteStates.countDocuments() should equal(1)
  }

  private def verifyUpdate_route(group: MonitorGroup, route: MonitorRoute): Unit = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        route._id,
        groupId = group._id,
        name = "route-name",
        description = "",
        comment = None,
        relationId = Some(route1.relationId), // relationId filled in
        user = "user",
        timestamp = UpdateTimestamp,
        symbol = None,
        analysisTimestamp = Some(UpdateTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(ReferenceTimestamp1),
        referenceFilename = None,
        referenceDistance = route1.meters,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        osmDistance = route1.meters,
        happy = true,
        relation = None
      )
    )
  }

  private def verifyUpdate_reference(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(route1.relationId)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = route._id,
        relationId = Some(route1.relationId),
        timestamp = UpdateTimestamp,
        user = "user",
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

  private def verifyUpdate_state(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 1).get
    assertEqual(
      state,
      MonitorRouteState(
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

  private def setup(): (MonitorGroup, MonitorUpdateReporterMock) = {

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, reporter)
  }

  private def setupLoadStructure(): Unit = {
    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route-name"
        ),
      )
    setupRouteStructure(Some(ReferenceTimestamp1), overpassData, 1)
  }

  private def setupLoadTopLevel(): Unit = {
    val overpassData = OverpassData()
      .node(1001, latitude = "51.4633666", longitude = "4.4553911")
      .node(1002, latitude = "51.4618272", longitude = "4.4562458")
      .way(101, 1001, 1002)
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route-name"
        ),
        members = Seq(
          newMember(MemberType.Way, 101),
        )
      )
    val relation = new DataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp1), 1).returns(Some(relation))
  }

  private def setupBaseRouteDoc(): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(1),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = "[[4.4553911, 51.4633666],[4.4562458,51.4618272]]"
          )
        )
      )
    )
  }
}
