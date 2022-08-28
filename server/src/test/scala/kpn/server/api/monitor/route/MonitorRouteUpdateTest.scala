package kpn.server.api.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteUpdateResult
import kpn.api.custom.Day
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.scalamock.scalatest.MockFactory

class MonitorRouteUpdateTest extends UnitTest with SharedTestObjects with MockFactory {

  test("add route with gpx reference") {
    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    val group = newMonitorGroup("group", "")
    (monitorGroupRepository.groupByName _).when("group").returns(Some(group))
    (monitorRouteRepository.routeByName _).when(group._id, "route").returns(None)

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      name = "route",
      description = "description",
      relationId = Some("1"),
      referenceType = "gpx",
      osmReferenceDay = None,
      gpxFileChanged = true,
      gpxFilename = Some("filename")
    )

    updater.add("user", "group", properties)

    (monitorRouteRepository.saveRoute _).verify(
      where { route: MonitorRoute =>
        route.groupId should equal(group._id)
        route.name should equal("route")
        route.description should equal("description")
        route.relationId should equal(Some(1L))
        true
      }
    ).once()

    (monitorRouteRepository.saveRouteReference _).verify(*).never()
    (monitorRouteAnalyzer.analyze _).verify(*, *).never()
  }


  test("add route with osm reference") {



  }


  test("route update - no changes") {
  }

  test("route update - name") {
    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    val group = newMonitorGroup("group", "")
    val route = newMonitorRoute(group._id, "route", "", Some(1L))
    val reference = newMonitorRouteReference(
      routeId = route._id,
      relationId = route.relationId,
      //      key = "",
      //      created = Time.now,
      //      user = "user",
      //      bounds = Bounds(),
      referenceType = "osm",
      osmReferenceDay = Some(Day(2022, 8, Some(11))),
      //      segmentCount = 3,
      //      filename = None,
      //      geometry = "geometry"
    )
    (monitorGroupRepository.groupByName _).when("group").returns(Some(group))
    (monitorRouteRepository.routeByName _).when(group._id, "route").returns(Some(route))
    (monitorRouteRepository.currentRouteReference _).when(route._id).returns(Some(reference))

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      name = "route-changed", // <-- changed
      description = "description-changed", // <-- changed
      relationId = Some("1"),
      referenceType = "osm",
      osmReferenceDay = Some(Day(2022, 8, Some(11))),
      gpxFileChanged = false,
      gpxFilename = None
    )

    val result = updater.update("user", "group", "route", properties)

    result should equal(MonitorRouteUpdateResult(reAnalyzed = false))

    val expectedUpdatedRoute = route.copy(
      name = "route-changed",
      description = "description-changed",
    )

    (monitorRouteRepository.saveRoute _).verify(expectedUpdatedRoute)
    (monitorRouteRepository.saveRouteReference _).verify(*).never()
    (monitorRouteAnalyzer.analyze _).verify(*, *).never()
  }

  test("route update - relationId") {
    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    val group = newMonitorGroup("group", "")
    val route = newMonitorRoute(group._id, "route", "", Some(1L))
    val reference = newMonitorRouteReference(
      routeId = route._id,
      relationId = route.relationId,
      //      key = "",
      //      created = Time.now,
      //      user = "user",
      //      bounds = Bounds(),
      referenceType = "osm",
      osmReferenceDay = Some(Day(2022, 8, Some(11))),
      //      segmentCount = 3,
      //      filename = None,
      //      geometry = "geometry"
    )
    (monitorGroupRepository.groupByName _).when("group").returns(Some(group))
    (monitorRouteRepository.routeByName _).when(group._id, "route").returns(Some(route))
    (monitorRouteRepository.currentRouteReference _).when(route._id).returns(Some(reference))

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      name = "route",
      description = "description",
      relationId = Some("2"), // <-- changed
      referenceType = "osm",
      osmReferenceDay = Some(Day(2022, 8, Some(11))),
      gpxFileChanged = false,
      gpxFilename = None
    )

    val result = updater.update("user", "group", "route", properties)

    result should equal(MonitorRouteUpdateResult(reAnalyzed = false))

    val expectedUpdatedRoute = route.copy(
      name = "route-changed",
      description = "description-changed",
    )

    (monitorRouteRepository.saveRoute _).verify(expectedUpdatedRoute)
    (monitorRouteRepository.saveRouteReference _).verify(*).never()
    (monitorRouteAnalyzer.analyze _).verify(*, *).never()
  }

  test("route update - group not found") {
    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    (monitorGroupRepository.groupByName _).when("group").returns(None)

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      name = "route",
      description = "description",
      relationId = Some("1"),
      referenceType = "osm",
      osmReferenceDay = Some(Day(2022, 8, Some(11))),
      gpxFileChanged = false,
      gpxFilename = None
    )

    intercept[IllegalArgumentException] {
      updater.update("user", "group", "route", properties)
    }.getMessage should equal("""Could not find group with name "group"""")

  }

  test("route update - route not found") {
    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    val group = newMonitorGroup("group", "")
    (monitorGroupRepository.groupByName _).when("group").returns(Some(group))
    (monitorRouteRepository.routeByName _).when(group._id, "route").returns(None)

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      name = "route",
      description = "description",
      relationId = Some("1"),
      referenceType = "osm",
      osmReferenceDay = Some(Day(2022, 8, Some(11))),
      gpxFileChanged = false,
      gpxFilename = None
    )

    val expectedMessage = s"""Could not find route with name "route" in group "${group._id.oid}""""
    intercept[IllegalArgumentException] {
      updater.update("user", "group", "route", properties)
    }.getMessage should equal(expectedMessage)
  }
}
