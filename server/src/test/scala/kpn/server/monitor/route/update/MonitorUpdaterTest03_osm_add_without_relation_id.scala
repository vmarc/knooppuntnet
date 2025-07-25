package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState

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
      MonitorUpdateArgs(
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
    configuration.routeRepository.saveBaseRoute(route1.baseRouteDoc)
    configuration.routeRepository.saveRoute(route1.routeDoc)

    val updatedUpdate = update.copy(
      action = MonitorAction.update,
      relationId = Some(route1.relationId),
    )

    Time.set(UpdateTimestamp)

    val reporter2 = new MonitorUpdateReporterMock()
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateArgs(
        "user",
        reporter,
        updatedUpdate
      )
    )
  }

  private def verifyAdd(group: MonitorGroup) = {
    verifyDocumentCounts(1, 0, 0)
    val route = verifyAdd_route(group)
    verifyAdd_noReference(route)
    verifyAdd_noState(route)
    route
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
        relationIds = Seq.empty,
        bounds = None,
        happy = false,
      )
    )
    route
  }

  private def verifyAdd_noReference(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.reference(route._id, Some(route1.relationId)) should equal(None)
  }

  private def verifyAdd_noState(route: MonitorRoute): Unit = {
    configuration.monitorRouteRepository.state(route._id, route1.relationId) should equal(None)
  }

  private def verifyUpdate(group: MonitorGroup, route: MonitorRoute): Unit = {
    verifyDocumentCounts(1, 1, 1)
    verifyUpdate_route(group, route)
    verifyUpdate_reference(route)
    verifyUpdate_state(route)
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
        relationIds = Seq(route1.relationId),
        bounds = Some(route1.bounds),
        happy = true
      )
    )
  }

  private def verifyUpdate_reference(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.reference(route._id, Some(route1.relationId)).get
    assertEqual(
      reference,
      MonitorReference(
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
        referenceLines = route1.lines,
        tiles = route1.referenceTiles
      )
    )
  }

  private def verifyUpdate_state(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.state(route._id, route1.relationId).get
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
        actualLines = Seq.empty
      )
    )
    val stateTiles = configuration.monitorRouteRepository.stateTiles(route._id, route1.relationId)
    assertEqual(
      stateTiles.map(MonitorStateTileInfo.from),
      route1.stateTiles
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
    val monitorRouteRelation = route1.overpassStructure
    (configuration.monitorRouteStructureLoader.load _).when(Some(ReferenceTimestamp1), route1.relationId).returns(Some(monitorRouteRelation))
  }

  private def setupLoadTopLevel(): Unit = {
    val relation = route1.overpassTopLevel
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, route1.relationId).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(ReferenceTimestamp1), route1.relationId).returns(Some(relation))
  }
}
