package kpn.server.api.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.common.monitor.MonitorRouteSubRelation
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.Log
import kpn.core.util.UnitTest
import kpn.server.api.monitor.domain.MonitorRouteOsmSegment
import kpn.server.api.monitor.domain.MonitorRouteOsmSegmentElement
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl

class MonitorRouteMapPageBuilderTest extends UnitTest with SharedTestObjects {

  test("unknown group") {

    withDatabase() { database =>

      val log = Log.mock
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val pageBuilder = new MonitorRouteMapPageBuilder(groupRepository, routeRepository)

      pageBuilder.build("group", "route", None, log) should equal(None)
      log.messages should equal(
        Seq(
          """ERROR Group "group" does not exist"""
        )
      )
    }
  }

  test("unknown route") {

    withDatabase() { database =>

      val log = Log.mock
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val pageBuilder = new MonitorRouteMapPageBuilder(groupRepository, routeRepository)

      val group = newMonitorGroup("group")
      groupRepository.saveGroup(group)

      pageBuilder.build("group", "route", None, log) should equal(None)
      log.messages should equal(
        Seq(
          """ERROR Route "route" does not exist"""
        )
      )
    }
  }

  test("relationId parameter not matching relationId in MonitorRoute") {

    withDatabase() { database =>

      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val pageBuilder = new MonitorRouteMapPageBuilder(groupRepository, routeRepository)

      val group = newMonitorGroup("group", "group-description")
      groupRepository.saveGroup(group)

      val route = newMonitorRoute(
        group._id,
        name = "route",
        description = "route-description",
        relationId = Some(1),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, 11)),
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
          )
        )
      )
      routeRepository.saveRoute(route)

      intercept[IllegalStateException] {
        pageBuilder.build("group", "route", Some(99))
      }.getMessage should equal("""Requested relationId "99" does not match route relationId "1"""")
    }
  }

  test("route without state or reference") {

    withDatabase() { database =>

      val log = Log.mock
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val pageBuilder = new MonitorRouteMapPageBuilder(groupRepository, routeRepository)

      val group = newMonitorGroup("group", "group-description")
      groupRepository.saveGroup(group)

      val route = newMonitorRoute(
        group._id,
        name = "route",
        description = "route-description",
        relationId = None,
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, 11))
      )

      routeRepository.saveRoute(route)

      pageBuilder.build("group", "route", None, log) shouldMatchTo {
        Some(
          MonitorRouteMapPage(
            relationId = None,
            routeName = "route",
            routeDescription = "route-description",
            groupName = "group",
            groupDescription = "group-description",
            bounds = None,
            nextSubRelation = None,
            prevSubRelation = None,
            osmSegments = Seq.empty,
            matchesGeoJson = None,
            deviations = Seq.empty,
            reference = None,
            subRelations = Seq.empty
          )
        )
      }
      log.messages should equal(Seq.empty)
    }
  }

  test("route with gpx reference but relation not defined yet") {

    withDatabase() { database =>

      val log = Log.mock
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val pageBuilder = new MonitorRouteMapPageBuilder(groupRepository, routeRepository)

      val group = newMonitorGroup("group", "group-description")
      groupRepository.saveGroup(group)

      val route = newMonitorRoute(
        group._id,
        name = "route",
        description = "route-description",
        relationId = None,
        referenceDay = Some(Day(2022, 8, 11))
      )
      routeRepository.saveRoute(route)

      val reference = newMonitorRouteReference(
        routeId = route._id,
        relationId = 0,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        user = "user",
        bounds = Bounds(1, 1, 1, 1),
        referenceType = "gpx",
        referenceDay = Day(2022, 8, 11),
        distance = 1000,
        segmentCount = 1,
        filename = Some("filename"),
        geoJson = "geo-json-reference"
      )
      routeRepository.saveRouteReference(reference)

      pageBuilder.build("group", "route", None, log) shouldMatchTo {
        Some(
          MonitorRouteMapPage(
            relationId = None,
            routeName = "route",
            routeDescription = "route-description",
            groupName = "group",
            groupDescription = "group-description",
            bounds = Some(Bounds(1, 1, 1, 1)),
            nextSubRelation = None,
            prevSubRelation = None,
            osmSegments = Seq.empty,
            matchesGeoJson = None,
            deviations = Seq.empty,
            reference = Some(
              MonitorRouteReferenceInfo(
                created = Timestamp(2022, 8, 11, 12, 0, 0),
                user = "user",
                bounds = Bounds(1, 1, 1, 1),
                distance = 1000,
                referenceType = "gpx",
                referenceDay = Day(2022, 8, 11),
                segmentCount = 1,
                gpxFilename = Some("filename"),
                geoJson = "geo-json-reference"
              )
            ),
            subRelations = Seq.empty
          )
        )
      }
      log.messages should equal(Seq.empty)
    }
  }

  test("route with osm reference and state") {

    withDatabase() { database =>

      val log = Log.mock
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val pageBuilder = new MonitorRouteMapPageBuilder(groupRepository, routeRepository)

      val group = newMonitorGroup("group", "group-description")
      groupRepository.saveGroup(group)

      val route = newMonitorRoute(
        group._id,
        name = "route",
        description = "route-description",
        relationId = Some(1),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, 11)),
        referenceDistance = 1000,
        deviationDistance = 100,
        deviationCount = 1,
        osmWayCount = 10,
        osmDistance = 1000,
        osmSegmentCount = 1,
        osmSegments = Seq(
          MonitorRouteOsmSegment(
            Seq(
              MonitorRouteOsmSegmentElement(
                relationId = 1,
                segmentId = 1,
                meters = 1000,
                bounds = Bounds(1, 1, 1, 1),
                reversed = false
              )
            )
          )
        ),
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
            name = "name-relation-1",
            deviationDistance = 100,
            deviationCount = 1,
            osmWayCount = 10,
            osmDistance = 1000,
            osmSegmentCount = 1,
            relations = Seq.empty
          )
        ),
        happy = true
      )

      val reference = newMonitorRouteReference(
        routeId = route._id,
        relationId = 1,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        user = "user",
        bounds = Bounds(1, 1, 1, 1),
        referenceType = "osm",
        referenceDay = Day(2022, 8, 11),
        distance = 1000,
        segmentCount = 1,
        filename = None,
        geoJson = "geo-json-reference"
      )

      val state = newMonitorRouteState(
        routeId = route._id,
        relationId = 1,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        wayCount = 10,
        osmDistance = 1000,
        bounds = Bounds(1, 1, 1, 1),
        osmSegments = Seq(
          MonitorRouteSegment(
            id = 1,
            startNodeId = 1001,
            endNodeId = 1002,
            meters = 100,
            bounds = Bounds(1, 1, 1, 1),
            geoJson = "geo-json-route-segment-11-1"
          )
        ),
        matchesGeometry = Some("matches-geometry"),
        deviations = Seq(
          MonitorRouteDeviation(
            1,
            meters = 100,
            distance = 12,
            bounds = Bounds(1, 1, 1, 1),
            geoJson = "geo-json-deviation-1"
          )
        )
      )

      routeRepository.saveRoute(route)
      routeRepository.saveRouteState(state)
      routeRepository.saveRouteReference(reference)

      val expectedPage = Some(
        MonitorRouteMapPage(
          relationId = Some(1),
          routeName = "route",
          routeDescription = "route-description",
          groupName = "group",
          groupDescription = "group-description",
          bounds = Some(Bounds(1, 1, 1, 1)),
          nextSubRelation = None,
          prevSubRelation = None,
          osmSegments = Seq(
            MonitorRouteSegment(
              id = 1,
              startNodeId = 1001,
              endNodeId = 1002,
              meters = 100,
              bounds = Bounds(1, 1, 1, 1),
              geoJson = "geo-json-route-segment-11-1"
            )
          ),
          matchesGeoJson = Some("matches-geometry"),
          deviations = Seq(
            MonitorRouteDeviation(
              1,
              meters = 100,
              distance = 12,
              bounds = Bounds(1, 1, 1, 1),
              geoJson = "geo-json-deviation-1"
            )
          ),
          reference = Some(
            MonitorRouteReferenceInfo(
              created = Timestamp(2022, 8, 11, 12, 0, 0),
              user = "user",
              bounds = Bounds(1, 1, 1, 1),
              distance = 1000,
              referenceType = "osm",
              referenceDay = Day(2022, 8, 11),
              segmentCount = 1,
              gpxFilename = None,
              geoJson = "geo-json-reference"
            )
          ),
          subRelations = Seq.empty
        )
      )

      pageBuilder.build("group", "route", None, log) shouldMatchTo expectedPage
      pageBuilder.build("group", "route", Some(1), log) shouldMatchTo expectedPage

      log.messages should equal(Seq.empty)
    }
  }

  test("multi-gpx without states") {

    withDatabase() { database =>
      val log = Log.mock
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val pageBuilder = new MonitorRouteMapPageBuilder(groupRepository, routeRepository)

      val group = newMonitorGroup("group", "group-description")
      groupRepository.saveGroup(group)

      val route = newMonitorRoute(
        group._id,
        name = "route",
        description = "route-description",
        relationId = None,
        referenceType = "multi-gpx",
        referenceDay = Some(Day(2022, 8, 11)),
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
            relations = Seq(
              newMonitorRouteRelation(
                relationId = 11,
                name = "sub-relation-11"
              ),
              newMonitorRouteRelation(
                relationId = 12,
                name = "sub-relation-12"
              )
            )
          )
        )
      )
      routeRepository.saveRoute(route)

      val reference11 = newMonitorRouteReference(
        routeId = route._id,
        relationId = 11,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        user = "user",
        bounds = Bounds(1, 1, 1, 1),
        referenceType = "gpx",
        referenceDay = Day(2022, 8, 11),
        distance = 1000,
        segmentCount = 1,
        filename = Some("filename-11"),
        geoJson = "geo-json-reference-11"
      )
      val reference12 = newMonitorRouteReference(
        routeId = route._id,
        relationId = 12,
        timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
        user = "user",
        bounds = Bounds(2, 2, 2, 2),
        referenceType = "gpx",
        referenceDay = Day(2022, 8, 12),
        distance = 1000,
        segmentCount = 1,
        filename = Some("filename-12"),
        geoJson = "geo-json-reference-12"
      )
      routeRepository.saveRouteReference(reference11)
      routeRepository.saveRouteReference(reference12)

      pageBuilder.build("group", "route", Some(11), log) shouldMatchTo {
        Some(
          MonitorRouteMapPage(
            relationId = None,
            routeName = "route",
            routeDescription = "route-description",
            groupName = "group",
            groupDescription = "group-description",
            bounds = Some(Bounds(1, 1, 1, 1)),
            nextSubRelation = Some(
              MonitorRouteSubRelation(
                12,
                "sub-relation-12"
              )
            ),
            prevSubRelation = None,
            osmSegments = Seq.empty,
            matchesGeoJson = None,
            deviations = Seq.empty,
            reference = Some(
              MonitorRouteReferenceInfo(
                created = Timestamp(2022, 8, 11, 12, 0, 0),
                user = "user",
                bounds = Bounds(1, 1, 1, 1),
                distance = 1000,
                referenceType = "gpx",
                referenceDay = Day(2022, 8, 11),
                segmentCount = 1,
                gpxFilename = Some("filename-11"),
                geoJson = "geo-json-reference-11"
              )
            ),
            subRelations = Seq(
              MonitorRouteSubRelation(11, "sub-relation-11"),
              MonitorRouteSubRelation(12, "sub-relation-12")
            )
          )
        )
      }

      pageBuilder.build("group", "route", Some(12), log) shouldMatchTo {
        Some(
          MonitorRouteMapPage(
            relationId = None,
            routeName = "route",
            routeDescription = "route-description",
            groupName = "group",
            groupDescription = "group-description",
            bounds = Some(Bounds(2, 2, 2, 2)),
            nextSubRelation = None,
            prevSubRelation = Some(
              MonitorRouteSubRelation(
                11,
                "sub-relation-11"
              )
            ),
            osmSegments = Seq.empty,
            matchesGeoJson = None,
            deviations = Seq.empty,
            reference = Some(
              MonitorRouteReferenceInfo(
                created = Timestamp(2022, 8, 12, 12, 0, 0),
                user = "user",
                bounds = Bounds(2, 2, 2, 2),
                distance = 1000,
                referenceType = "gpx",
                referenceDay = Day(2022, 8, 12),
                segmentCount = 1,
                gpxFilename = Some("filename-12"),
                geoJson = "geo-json-reference-12"
              )
            ),
            subRelations = Seq(
              MonitorRouteSubRelation(11, "sub-relation-11"),
              MonitorRouteSubRelation(12, "sub-relation-12")
            )
          )
        )
      }

      log.messages should equal(Seq.empty)
    }
  }

  test("multi-gpx with states") {

    withDatabase() { database =>
      val log = Log.mock
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val pageBuilder = new MonitorRouteMapPageBuilder(groupRepository, routeRepository)

      val group = newMonitorGroup("group", "group-description")
      groupRepository.saveGroup(group)

      val route = newMonitorRoute(
        group._id,
        name = "route",
        description = "route-description",
        relationId = None,
        referenceType = "multi-gpx",
        referenceDay = Some(Day(2022, 8, 11)),
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
            relations = Seq(
              newMonitorRouteRelation(
                relationId = 11,
                name = "sub-relation-11"
              ),
              newMonitorRouteRelation(
                relationId = 12,
                name = "sub-relation-12"
              )
            )
          )
        )
      )
      routeRepository.saveRoute(route)

      val reference11 = newMonitorRouteReference(
        routeId = route._id,
        relationId = 11,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        user = "user",
        bounds = Bounds(1, 1, 1, 1),
        referenceType = "gpx",
        referenceDay = Day(2022, 8, 11),
        distance = 1000,
        segmentCount = 1,
        filename = Some("filename-11"),
        geoJson = "geo-json-reference-11"
      )

      val state11 = newMonitorRouteState(
        routeId = route._id,
        relationId = 11,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        wayCount = 111,
        osmDistance = 1011,
        bounds = Bounds(1, 1, 1, 1),
        osmSegments = Seq(
          MonitorRouteSegment(
            id = 1,
            startNodeId = 1001,
            endNodeId = 1002,
            meters = 100,
            bounds = Bounds(1, 1, 1, 1),
            geoJson = "geo-json-route-segment-11-1"
          )
        ),
        matchesGeometry = Some("matches-geometry-11"),
        deviations = Seq(
          MonitorRouteDeviation(
            id = 1,
            meters = 11,
            distance = 111,
            bounds = Bounds(2, 2, 2, 2),
            geoJson = "geo-json-deviation-11-1"

          )
        ),
        happy = true
      )

      val reference12 = newMonitorRouteReference(
        routeId = route._id,
        relationId = 12,
        timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
        user = "user",
        bounds = Bounds(2, 2, 2, 2),
        referenceType = "gpx",
        referenceDay = Day(2022, 8, 12),
        distance = 1000,
        segmentCount = 1,
        filename = Some("filename-12"),
        geoJson = "geo-json-reference-12"
      )
      val state12 = newMonitorRouteState(
        routeId = route._id,
        relationId = 12,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        wayCount = 112,
        osmDistance = 1012,
        bounds = Bounds(1, 1, 1, 1),
        osmSegments = Seq(
          MonitorRouteSegment(
            id = 1,
            startNodeId = 2001,
            endNodeId = 2002,
            meters = 200,
            bounds = Bounds(1, 1, 1, 1),
            geoJson = "geo-json-route-segment-12-1"
          )
        ),
        matchesGeometry = Some("matches-geometry-12"),
        deviations = Seq(
          MonitorRouteDeviation(
            id = 1,
            meters = 11,
            distance = 111,
            bounds = Bounds(2, 2, 2, 2),
            geoJson = "geo-json-deviation-12-1"
          )
        ),
        happy = true
      )
      routeRepository.saveRouteReference(reference11)
      routeRepository.saveRouteReference(reference12)
      routeRepository.saveRouteState(state11)
      routeRepository.saveRouteState(state12)

      val expectedPage11 = Some(
        MonitorRouteMapPage(
          relationId = None,
          routeName = "route",
          routeDescription = "route-description",
          groupName = "group",
          groupDescription = "group-description",
          bounds = Some(Bounds(1, 1, 1, 1)),
          prevSubRelation = None,
          nextSubRelation = Some(
            MonitorRouteSubRelation(
              12,
              "sub-relation-12"
            )
          ),
          osmSegments = Seq(
            MonitorRouteSegment(
              id = 1,
              startNodeId = 1001,
              endNodeId = 1002,
              meters = 100,
              bounds = Bounds(1, 1, 1, 1),
              geoJson = "geo-json-route-segment-11-1"
            )
          ),
          matchesGeoJson = Some("matches-geometry-11"),
          deviations = Seq(
            MonitorRouteDeviation(
              id = 1,
              meters = 11,
              distance = 111,
              bounds = Bounds(2, 2, 2, 2),
              geoJson = "geo-json-deviation-11-1"

            )
          ),
          reference = Some(
            MonitorRouteReferenceInfo(
              created = Timestamp(2022, 8, 11, 12, 0, 0),
              user = "user",
              bounds = Bounds(1, 1, 1, 1),
              distance = 1000,
              referenceType = "gpx",
              referenceDay = Day(2022, 8, 11),
              segmentCount = 1,
              gpxFilename = Some("filename-11"),
              geoJson = "geo-json-reference-11"
            )
          ),
          subRelations = Seq(
            MonitorRouteSubRelation(11, "sub-relation-11"),
            MonitorRouteSubRelation(12, "sub-relation-12")
          )
        )
      )

      val expectedPage12 = Some(
        MonitorRouteMapPage(
          relationId = None,
          routeName = "route",
          routeDescription = "route-description",
          groupName = "group",
          groupDescription = "group-description",
          bounds = Some(Bounds(1, 1, 2, 2)),
          prevSubRelation = Some(
            MonitorRouteSubRelation(
              11,
              "sub-relation-11"
            )
          ),
          nextSubRelation = None,
          osmSegments = Seq(
            MonitorRouteSegment(
              id = 1,
              startNodeId = 2001,
              endNodeId = 2002,
              meters = 200,
              bounds = Bounds(1, 1, 1, 1),
              geoJson = "geo-json-route-segment-12-1"
            )
          ),
          matchesGeoJson = Some("matches-geometry-12"),
          deviations = Seq(
            MonitorRouteDeviation(
              id = 1,
              meters = 11,
              distance = 111,
              bounds = Bounds(2, 2, 2, 2),
              geoJson = "geo-json-deviation-12-1"
            )
          ),
          reference = Some(
            MonitorRouteReferenceInfo(
              created = Timestamp(2022, 8, 12, 12, 0, 0),
              user = "user",
              bounds = Bounds(2, 2, 2, 2),
              distance = 1000,
              referenceType = "gpx",
              referenceDay = Day(2022, 8, 12),
              segmentCount = 1,
              gpxFilename = Some("filename-12"),
              geoJson = "geo-json-reference-12"
            )
          ),
          subRelations = Seq(
            MonitorRouteSubRelation(11, "sub-relation-11"),
            MonitorRouteSubRelation(12, "sub-relation-12")
          )
        )
      )

      pageBuilder.build("group", "route", None, log) shouldMatchTo expectedPage11
      pageBuilder.build("group", "route", Some(11), log) shouldMatchTo expectedPage11
      pageBuilder.build("group", "route", Some(12), log) shouldMatchTo expectedPage12

      log.messages should equal(Seq.empty)
    }
  }
}
