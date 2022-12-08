package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.TestData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.scalamock.scalatest.MockFactory

class MonitorRouteUpdateTest extends UnitTest with SharedTestObjects with MockFactory {

  test("add route with gpx reference") {

    val group = newMonitorGroup("group", "")

    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    (monitorGroupRepository.groupByName _).when("group").returns(Some(group))
    (monitorRouteRepository.routeByName _).when(group._id, "route").returns(None)

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      groupName = group.name,
      name = "route",
      description = "description",
      comment = None,
      relationId = Some(1L),
      referenceType = Some("gpx"),
      referenceDay = None,
      referenceFileChanged = true,
      referenceFilename = Some("filename")
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
    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    val group = newMonitorGroup("group", "")
    (monitorGroupRepository.groupByName _).when("group").returns(Some(group))
    (monitorRouteRepository.routeByName _).when(group._id, "route").returns(None)
    (monitorRouteRelationRepository.load _).when(Some(Timestamp(2022, 8, 11, 0, 0, 0)), 1L).returns(buildRelation())

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      groupName = group.name,
      name = "route",
      description = "description",
      comment = None,
      relationId = Some(1L),
      referenceType = Some("osm"),
      referenceDay = Some(Day(2022, 8, 11)),
      referenceFileChanged = false,
      referenceFilename = None
    )

    updater.add("user", "group", properties)

    var savedRoute: MonitorRoute = null
    var savedReference: MonitorRouteReference = null

    (monitorRouteRepository.saveRoute _).verify(
      where { route: MonitorRoute =>
        savedRoute = route
        route.groupId should equal(group._id)
        route.name should equal("route")
        route.description should equal("description")
        route.relationId should equal(Some(1L))
        true
      }
    ).once()

    (monitorRouteRepository.saveRouteReference _).verify(
      where { reference: MonitorRouteReference =>
        savedReference = reference
        reference.routeId should equal(savedRoute._id)
        reference.relationId should equal(Some(1L))
        reference.user should equal("user")
        reference.bounds should equal(Bounds(1, 1, 3, 3))
        reference.referenceType should equal("osm")
        reference.referenceDay should equal(Some(Day(2022, 8, 11)))
        reference.segmentCount should equal(1)
        reference.filename should equal(None)
        reference.geometry should equal("""{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[1,1],[2,2],[3,3]]}]}""")
        true
      }
    ).once()

    (monitorRouteAnalyzer.analyze _).verify(
      where { (route, reference) =>
        route should equal(savedRoute)
        reference should equal(savedReference)
        true
      }
    ).once()
  }

  test("route update - no changes") {

    val group = newMonitorGroup("group", "")

    val route = newMonitorRoute(
      group._id,
      name = "route",
      description = "description",
      relationId = Some(1L)
    )

    val reference = MonitorRouteReference(
      ObjectId(),
      routeId = route._id,
      relationId = Some(1L),
      created = Time.now,
      "user",
      bounds = Bounds(1, 1, 3, 3),
      referenceType = "osm",
      referenceDay = Some(Day(2022, 8, 11)),
      segmentCount = 1,
      filename = None,
      geometry = "bla"
    )

    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    (monitorGroupRepository.groupByName _).when("group").returns(Some(group))
    (monitorRouteRepository.routeByName _).when(group._id, "route").returns(Some(route))
    (monitorRouteRepository.routeReferenceRouteWithId _).when(route._id).returns(Some(reference))

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      groupName = group.name,
      name = "route",
      description = "description",
      comment = None,
      relationId = Some(1L),
      referenceType = Some("osm"),
      referenceDay = Some(Day(2022, 8, 11)),
      referenceFileChanged = false,
      referenceFilename = None
    )

    updater.update("user", "group", "route", properties)

    (monitorRouteRepository.saveRoute _).verify(*).never()
    (monitorRouteRepository.saveRouteReference _).verify(*).never()
    (monitorRouteRelationRepository.load _).verify(*, *).never()
    (monitorRouteAnalyzer.analyze _).verify(*, *).never()
  }

  test("route update - name") {

    val group = newMonitorGroup("group", "")
    val route = newMonitorRoute(group._id, "route", "", None, Some(1L))
    val reference = newMonitorRouteReference(
      routeId = route._id,
      relationId = route.relationId,
      referenceType = "osm",
      referenceDay = Some(Day(2022, 8, 11)),
    )

    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    (monitorGroupRepository.groupByName _).when("group").returns(Some(group))
    (monitorRouteRepository.routeByName _).when(group._id, "route").returns(Some(route))
    (monitorRouteRepository.routeReferenceRouteWithId _).when(route._id).returns(Some(reference))

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      groupName = group.name,
      name = "route-changed", // <-- changed
      description = "description-changed", // <-- changed
      comment = None,
      relationId = Some(1L),
      referenceType = Some("osm"),
      referenceDay = Some(Day(2022, 8, Some(11))),
      referenceFileChanged = false,
      referenceFilename = None
    )

    val result = updater.update("user", "group", "route", properties)

    result should equal(MonitorRouteSaveResult())

    val expectedUpdatedRoute = route.copy(
      name = "route-changed",
      description = "description-changed",
    )

    (monitorRouteRepository.saveRoute _).verify(expectedUpdatedRoute)
    (monitorRouteRepository.saveRouteReference _).verify(*).never()
    (monitorRouteAnalyzer.analyze _).verify(*, *).never()
  }

  test("route update - change group") {

    val group1 = newMonitorGroup("group1", "")
    val group2 = newMonitorGroup("group2", "")
    val route = newMonitorRoute(group1._id, "route", "", None, Some(1L))
    val reference = newMonitorRouteReference(
      routeId = route._id,
      relationId = route.relationId,
      referenceType = "osm",
      referenceDay = Some(Day(2022, 8, 11)),
    )

    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    (monitorGroupRepository.groupByName _).when("group1").returns(Some(group1))
    (monitorGroupRepository.groupByName _).when("group2").returns(Some(group2))
    (monitorRouteRepository.routeByName _).when(group1._id, "route").returns(Some(route))
    (monitorRouteRepository.routeReferenceRouteWithId _).when(route._id).returns(Some(reference))

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      groupName = group2.name, // <-- changed
      name = "route",
      description = "",
      comment = None,
      relationId = Some(1L),
      referenceType = Some("osm"),
      referenceDay = Some(Day(2022, 8, Some(11))),
      referenceFileChanged = false,
      referenceFilename = None
    )

    val result = updater.update("user", "group1", "route", properties)

    result should equal(MonitorRouteSaveResult())

    val expectedUpdatedRoute = route.copy(
      groupId = group2._id
    )

    (monitorRouteRepository.saveRoute _).verify(expectedUpdatedRoute)
    (monitorRouteRepository.saveRouteReference _).verify(*).never()
    (monitorRouteAnalyzer.analyze _).verify(*, *).never()
  }

  test("route update - relationId") {

    val group = newMonitorGroup("group", "")

    val route = newMonitorRoute(
      group._id,
      name = "route",
      description = "description",
      relationId = Some(1L) // <-- original relation id
    )

    val reference = MonitorRouteReference(
      ObjectId(),
      routeId = route._id,
      relationId = Some(1L), // <-- original relation id
      created = Time.now,
      "user",
      bounds = Bounds(1, 1, 3, 3),
      referenceType = "osm",
      referenceDay = Some(Day(2022, 8, 11)),
      segmentCount = 1,
      filename = None,
      geometry = "bla"
    )

    val monitorGroupRepository = stub[MonitorGroupRepository]
    val monitorRouteRepository = stub[MonitorRouteRepository]
    val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
    val monitorRouteAnalyzer = stub[MonitorRouteAnalyzer]

    (monitorGroupRepository.groupByName _).when("group").returns(Some(group))
    (monitorRouteRepository.routeByName _).when(group._id, "route").returns(Some(route))
    (monitorRouteRepository.routeReferenceRouteWithId _).when(route._id).returns(Some(reference))
    (monitorRouteRelationRepository.load _).when(Some(Timestamp(2022, 8, 11, 0, 0, 0)), 2L).returns(buildOtherRelation())

    val updater = new MonitorRouteUpdater(
      monitorGroupRepository,
      monitorRouteRepository,
      monitorRouteRelationRepository,
      monitorRouteAnalyzer
    )

    val properties = MonitorRouteProperties(
      groupName = group.name,
      name = "route",
      description = "description",
      comment = None,
      relationId = Some(2L), // <-- changed
      referenceType = Some("osm"),
      referenceDay = Some(Day(2022, 8, Some(11))),
      referenceFileChanged = false,
      referenceFilename = None
    )

    val result = updater.update("user", "group", "route", properties)

    result should equal(MonitorRouteSaveResult(analyzed = true))

    val expectedUpdatedRoute = route.copy(
      relationId = Some(2L)
    )

    (monitorRouteRepository.saveRoute _).verify(expectedUpdatedRoute)

    var savedReference: MonitorRouteReference = null

    (monitorRouteRepository.saveRouteReference _).verify(
      where { reference: MonitorRouteReference =>
        savedReference = reference
        reference.routeId should equal(route._id)
        reference.relationId should equal(Some(2L))
        reference.user should equal("user")
        reference.bounds should equal(Bounds(1, 1, 2, 2))
        reference.referenceType should equal("osm")
        reference.referenceDay should equal(Some(Day(2022, 8, 11)))
        reference.segmentCount should equal(1)
        reference.filename should equal(None)
        reference.geometry should equal("""{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[1,1],[2,2]]}]}""")
        true
      }
    ).once()

    (monitorRouteAnalyzer.analyze _).verify(
      where { (route, reference) =>
        route should equal(expectedUpdatedRoute)
        reference should equal(savedReference)
        true
      }
    ).once()
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
      groupName = "group",
      name = "route",
      description = "description",
      comment = None,
      relationId = Some(1L),
      referenceType = Some("osm"),
      referenceDay = Some(Day(2022, 8, 11)),
      referenceFileChanged = false,
      referenceFilename = None
    )

    intercept[IllegalArgumentException] {
      updater.update("user", "group", "route", properties)
    }.getMessage should equal("""[route-update, group=group, route=route] Could not find group with name "group"""")

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
      groupName = group.name,
      name = "route",
      description = "description",
      comment = None,
      relationId = Some(1L),
      referenceType = Some("osm"),
      referenceDay = Some(Day(2022, 8, Some(11))),
      referenceFileChanged = false,
      referenceFilename = None
    )

    val expectedMessage = s"""[route-update, group=group, route=route] Could not find route with name "route" in group "${group._id.oid}""""
    intercept[IllegalArgumentException] {
      updater.update("user", "group", "route", properties)
    }.getMessage should equal(expectedMessage)
  }

  private def buildRelation(): Option[Relation] = {
    new TestData() {
      node(1001, latitude = "1", longitude = "1")
      node(1002, latitude = "2", longitude = "2")
      node(1003, latitude = "3", longitude = "3")
      way(101, 1001, 1002)
      way(102, 1002, 1003)
      relation(
        1L,
        Seq(
          newMember("way", 101),
          newMember("way", 102)
        ),
      )
    }.data.relations.get(1L)
  }

  private def buildOtherRelation(): Option[Relation] = {
    new TestData() {
      node(1001, latitude = "1", longitude = "1")
      node(1002, latitude = "2", longitude = "2")
      way(101, 1001, 1002)
      relation(
        2L,
        Seq(
          newMember("way", 101),
        ),
      )
    }.data.relations.get(2L)
  }
}
