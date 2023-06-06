package kpn.server.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteOsmSegment
import kpn.server.monitor.domain.MonitorRouteOsmSegmentElement
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest03_osm_add_super_route extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add superroute osm reference") {

    val referenceTimestamp = Timestamp(2022, 8, 11)

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)
      setupLoadStructure(configuration)
      setupLoadTopLevel(configuration, referenceTimestamp)

      val group = newMonitorGroup("group")
      configuration.monitorGroupRepository.saveGroup(group)

      val update = MonitorRouteUpdate(
        action = "add",
        groupName = group.name,
        routeName = "route-name",
        referenceType = "osm",
        description = Some("route-description"),
        comment = Some("route-comment"),
        relationId = Some(1),
        referenceTimestamp = Some(referenceTimestamp),
      )

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))

      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorUpdater.update("user", update, reporter)

      val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.shouldMatchTo(
        MonitorRoute(
          route._id,
          groupId = group._id,
          name = "route-name",
          description = "route-description",
          comment = Some("route-comment"),
          relationId = Some(1),
          user = "user",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          referenceType = "osm",
          referenceTimestamp = Some(referenceTimestamp),
          referenceFilename = None,
          referenceDistance = 335,
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 2,
          osmDistance = 335,
          osmSegmentCount = 1,
          happy = true,
          osmSegments = Seq(
            MonitorRouteOsmSegment(
              Seq(
                MonitorRouteOsmSegmentElement(
                  relationId = 11,
                  segmentId = 1,
                  meters = 196,
                  bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
                  reversed = false
                ),
                MonitorRouteOsmSegmentElement(
                  relationId = 12,
                  segmentId = 1,
                  meters = 139,
                  bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
                  reversed = false
                ),
              )
            )
          ),
          relation = Some(
            MonitorRouteRelation(
              relationId = 1,
              name = "main-relation",
              role = None,
              survey = None,
              referenceTimestamp = None,
              referenceFilename = None,
              referenceDistance = 0,
              deviationDistance = 0,
              deviationCount = 0,
              osmWayCount = 0,
              osmDistance = 0,
              osmSegmentCount = 0,
              happy = false,
              relations = Seq(
                MonitorRouteRelation(
                  relationId = 11,
                  name = "sub-relation-1",
                  role = None,
                  survey = None,
                  referenceTimestamp = None,
                  referenceFilename = None,
                  referenceDistance = 0,
                  deviationDistance = 0,
                  deviationCount = 0,
                  osmWayCount = 1,
                  osmDistance = 196,
                  osmSegmentCount = 1,
                  happy = true,
                  relations = Seq.empty
                ),
                MonitorRouteRelation(
                  relationId = 12,
                  name = "sub-relation-2",
                  role = None,
                  survey = None,
                  referenceTimestamp = None,
                  referenceFilename = None,
                  referenceDistance = 0,
                  deviationDistance = 0,
                  deviationCount = 0,
                  osmWayCount = 1,
                  osmDistance = 139,
                  osmSegmentCount = 1,
                  happy = true,
                  relations = Seq.empty
                )
              )
            )
          )
        )
      )

      val reference1 = configuration.monitorRouteRepository.routeRelationReference(route._id, 1)
      reference1 should equal(None)


      val reference11 = configuration.monitorRouteRepository.routeRelationReference(route._id, 11).get
      reference11.shouldMatchTo(
        MonitorRouteReference(
          reference11._id,
          routeId = route._id,
          relationId = Some(11),
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          user = "user",
          bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = "osm",
          referenceTimestamp = Timestamp(2022, 8, 11),
          distance = 196,
          segmentCount = 1,
          filename = None,
          geoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
        )
      )

      val reference12 = configuration.monitorRouteRepository.routeRelationReference(route._id, 12).get
      reference12.shouldMatchTo(
        MonitorRouteReference(
          reference12._id,
          routeId = route._id,
          relationId = Some(12),
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          user = "user",
          bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
          referenceType = "osm",
          referenceTimestamp = Timestamp(2022, 8, 11),
          distance = 139,
          segmentCount = 1,
          filename = None,
          geoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}]}"""
        )
      )

      val state = configuration.monitorRouteRepository.routeState(route._id, 1)
      state should equal(None)

      val state11 = configuration.monitorRouteRepository.routeState(route._id, 11).get
      state11.shouldMatchTo(
        MonitorRouteState(
          state11._id,
          routeId = route._id,
          relationId = 11,
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          wayCount = 1,
          osmDistance = 196,
          bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          osmSegments = Seq(
            MonitorRouteSegment(
              1,
              1001,
              1002,
              196,
              Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
              """{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
            )
          ),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
          deviations = Seq.empty,
          happy = true
        )
      )

      val state12 = configuration.monitorRouteRepository.routeState(route._id, 12).get
      state12.shouldMatchTo(
        MonitorRouteState(
          state12._id,
          routeId = route._id,
          relationId = 12,
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          wayCount = 1,
          osmDistance = 139,
          bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
          osmSegments = Seq(
            MonitorRouteSegment(
              1,
              1002,
              1003,
              139,
              bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
              """{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
            )
          ),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4562458,51.4618272],[4.455056,51.4614496]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
          deviations = Seq.empty,
          happy = true
        )
      )
    }
  }

  private def setupLoadStructure(configuration: MonitorUpdaterConfiguration): Unit = {

    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "main-relation"
        ),
        members = Seq(
          newMember("relation", 11),
          newMember("relation", 12)
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

    setupRouteStructure(configuration, overpassData, 1)
  }

  private def setupLoadTopLevel(configuration: MonitorUpdaterConfiguration, referenceTimestamp: Timestamp): Unit = {

    val overpassData = OverpassData()
      .node(1001, latitude = "51.4633666", longitude = "4.4553911")
      .node(1002, latitude = "51.4618272", longitude = "4.4562458")
      .node(1003, latitude = "51.4614496", longitude = "4.4550560")
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .relation(
        11,
        tags = Tags.from(
          "name" -> "sub-relation-1"
        ),
        members = Seq(
          newMember("way", 101),
        )
      )
      .relation(
        12,
        tags = Tags.from(
          "name" -> "sub-relation-2"
        ),
        members = Seq(
          newMember("way", 102),
        )
      )

    val data = new DataBuilder(overpassData.rawData).data
    val subRelation1 = data.relations(11)
    val subRelation2 = data.relations(12)

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(referenceTimestamp), 11).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(referenceTimestamp), 12).returns(Some(subRelation2))

    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 11).returns(Some(subRelation1))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 12).returns(Some(subRelation2))
  }
}
