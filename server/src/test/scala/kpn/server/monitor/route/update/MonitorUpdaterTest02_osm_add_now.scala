package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.common.Time
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest02_osm_add_now extends MonitorUpdateTest {

  private var route1: MonitorTestRoute = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    route1 = MonitorTestData.route1
  }

  test("osm reference at current system time") {

    val (group, reporter) = setup()

    executeAdd(group, reporter)

    verifyDocumentCounts(1, 1, 1)
    val route = verifyRoute(group)
    verifyReference(route)
    verifyState(route)
    verifyReporterMessages(reporter)
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
          relationId = Some(route1.relationId),
          referenceNow = Some(true),
        )
      )
    )
  }

  private def verifyRoute(group: MonitorGroup): MonitorRoute = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        _id = route._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(route1.relationId),
        user = "user",
        timestamp = CurrentTimestamp,
        symbol = None,
        analysisTimestamp = Some(CurrentTimestamp),
        analysisDuration = None,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(CurrentTimestamp),
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

  private def verifyReference(route: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(route._id, Some(route1.relationId)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        _id = reference._id,
        routeId = route._id,
        relationId = Some(route1.relationId),
        timestamp = CurrentTimestamp,
        user = "user",
        referenceBounds = route1.bounds,
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = CurrentTimestamp,
        referenceDistance = route1.meters,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceLines = route1.lines
      )
    )
  }

  private def verifyState(route: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(route._id, 1).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = route._id,
        relationId = 1,
        timestamp = CurrentTimestamp,
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        deviations = Seq.empty,
        matchesDistance = route1.meters,
        matchesLines = route1.lines,
      )
    )
  }

  private def verifyReporterMessages(reporter: MonitorUpdateReporterMock): Unit = {
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
          add("1", Some("1/1 route-name")),
          add("save")
        ),
        message(
          active("1")
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

  private def setup(): (MonitorGroup, MonitorUpdateReporterMock) = {
    configuration.routeRepository.saveBaseRoute(route1.baseRouteDoc)
    configuration.routeRepository.saveRoute(route1.routeDoc)

    val group = newMonitorGroup("group")
    configuration.monitorGroupRepository.saveGroup(group)

    Time.set(CurrentTimestamp)

    val reporter = new MonitorUpdateReporterMock()
    (group, reporter)
  }
}
