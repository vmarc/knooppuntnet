package kpn.server.api.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.api.monitor.MonitorRelationDataBuilder
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest01 extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add simple route with osm reference") {

    withDatabase() { database =>

      val config = new MonitorUpdaterConfiguration(database)
      setupLoadStructure(config)
      setupLoadTopLevel(config)

      val group = newMonitorGroup("group")
      config.monitorGroupRepository.saveGroup(group)

      val properties = MonitorRouteProperties(
        group.name,
        "route-name",
        "route-description",
        Some("route-comment"),
        Some(1L),
        "osm",
        Some(Day(2022, 8, 1)),
        None,
        referenceFileChanged = false,
      )

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      val saveResult = config.monitorUpdater.add("user", group.name, properties)

      saveResult should equal(
        MonitorRouteSaveResult(
          analyzed = true
        )
      )

      val route = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.shouldMatchTo(
        MonitorRoute(
          _id = route._id,
          groupId = group._id,
          name = "route-name",
          description = "route-description",
          comment = Some("route-comment"),
          relationId = Some(1L),
          user = "user",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          referenceType = "osm",
          referenceDay = Some(Day(2022, 8, 1)),
          referenceFilename = None,
          referenceDistance = 196L,
          deviationDistance = 0L,
          deviationCount = 0L,
          osmWayCount = 1L,
          osmDistance = 196L,
          osmSegmentCount = 1L,
          happy = true,
          superRouteOsmSegments = Seq.empty, // TODO ???
          relation = Some(
            MonitorRouteRelation(
              relationId = 1L,
              name = "route-name",
              role = None,
              survey = None,
              deviationDistance = 0L,
              deviationCount = 0L,
              osmWayCount = 1L,
              osmDistance = 196L,
              osmSegmentCount = 1L,
              happy = true,
              relations = Seq.empty
            )
          )
        )
      )

      val reference = config.monitorRouteRepository.routeRelationReference(route._id, 1L).get
      reference.shouldMatchTo(
        MonitorRouteReference(
          _id = reference._id,
          routeId = route._id,
          relationId = Some(1L),
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          user = "user",
          bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = "osm",
          referenceDay = Day(2022, 8, 1),
          distance = 196L,
          segmentCount = 1L,
          filename = None,
          geometry = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
        )
      )

      val state = config.monitorRouteRepository.routeState(route._id, 1L).get
      state.shouldMatchTo(
        MonitorRouteState(
          state._id,
          routeId = route._id,
          relationId = 1L,
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          wayCount = 1L,
          osmDistance = 196L,
          bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          osmSegments = Seq(
            MonitorRouteSegment(
              1L,
              1001L,
              1002L,
              196L,
              Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
              """{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
            )
          ),
          matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
          deviations = Seq.empty,
          happy = true
        )
      )
    }
  }

  private def setupLoadStructure(config: MonitorUpdaterConfiguration): Unit = {

    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route-name"
        ),
      )

    val relation = new MonitorRelationDataBuilder(overpassData.rawData).data.relations(1L)
    (config.monitorRouteRelationRepository.loadStructure _).when(None, 1L).returns(Some(relation))
  }

  private def setupLoadTopLevel(config: MonitorUpdaterConfiguration): Unit = {

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
          newMember("way", 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(1L)
    (config.monitorRouteRelationRepository.loadTopLevel _).when(None, 1L).returns(Some(relation))
    (config.monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(2022, 8, 1)), 1L).returns(Some(relation))
  }
}
