package kpn.server.api.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
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
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest02 extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def beforeEach(): Unit = {
    Time.set(Timestamp(2023, 1, 1))
  }

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add simple route with osm reference, but first without relationId") {

    withDatabase() { database =>

      val config = new MonitorUpdaterConfiguration(database)
      val group = newMonitorGroup("group")
      config.monitorGroupRepository.saveGroup(group)

      val properties = MonitorRouteProperties(
        group.name,
        "route-name",
        "",
        None,
        None,
        "osm",
        Some(Day(2022, 12, 1)),
        None,
        referenceFileChanged = false,
      )
      val saveResult = config.monitorUpdater.add("user", group.name, properties)
      saveResult should equal(MonitorRouteSaveResult())

      val route = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.groupId should equal(group._id)
      route.name should equal("route-name")
      route.description should equal("")
      route.comment should equal(None)
      route.relationId should equal(None)
      route.user should equal("user")
      route.referenceType should equal("osm")
      route.referenceDay should equal(Some(Day(2022, 12, 1)))
      route.referenceFilename should equal(None)
      route.referenceDistance should equal(0L)
      route.deviationDistance should equal(0L)
      route.deviationCount should equal(0L)
      route.osmWayCount should equal(0L)
      route.osmDistance should equal(0L)
      route.osmSegmentCount should equal(0L)
      route.happy should equal(false)

      route.relation should equal(None)

      config.monitorRouteRepository.routeRelationReference(route._id, 1L) should equal(None)
      config.monitorRouteRepository.routeState(route._id, 1L) should equal(None)

      setupLoadStructure(config)
      setupLoadTopLevel(config)

      val updatedProperties = properties.copy(
        relationId = Some(1)
      )

      val updateSaveResult = config.monitorUpdater.update("user", group.name, route.name, updatedProperties)
      updateSaveResult should equal(
        MonitorRouteSaveResult(
          analyzed = true,
        )
      )

      val updatedRoute = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      updatedRoute.groupId should equal(group._id)
      updatedRoute.name should equal("route-name")
      updatedRoute.description should equal("")
      updatedRoute.comment should equal(None)
      updatedRoute.relationId should equal(Some(1L))
      updatedRoute.user should equal("user")
      updatedRoute.referenceType should equal("osm")
      updatedRoute.referenceDay should equal(Some(Day(2022, 12, 1)))
      updatedRoute.referenceFilename should equal(None)
      updatedRoute.referenceDistance should equal(196L)
      updatedRoute.deviationDistance should equal(0L)
      updatedRoute.deviationCount should equal(0L)
      updatedRoute.osmWayCount should equal(1L)
      updatedRoute.osmDistance should equal(196L)
      updatedRoute.osmSegmentCount should equal(1L)
      updatedRoute.happy should equal(true)

      val updatedRelation = updatedRoute.relation.get
      updatedRelation.relationId should equal(1L)
      updatedRelation.name should equal("route-name")
      updatedRelation.role should equal(None)
      updatedRelation.survey should equal(None)
      updatedRelation.deviationDistance should equal(0L)
      updatedRelation.deviationCount should equal(0L)
      updatedRelation.osmWayCount should equal(1L)
      updatedRelation.osmDistance should equal(196L)
      updatedRelation.osmSegmentCount should equal(1)
      updatedRelation.happy should equal(true)
      updatedRelation.relations.size should equal(0)

      val reference = config.monitorRouteRepository.routeRelationReference(route._id, 1L).get
      reference.routeId should equal(route._id)
      reference.relationId should equal(Some(1L))
      reference.distance should equal(196L)
      reference.bounds should equal(Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458))
      reference.segmentCount should equal(1)
      reference.geometry should equal("""{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}""")

      val state = config.monitorRouteRepository.routeState(route._id, 1L).get
      state.routeId should equal(route._id)
      state.relationId should equal(1L)
      state.timestamp should equal(Timestamp(2023, 1, 1))
      state.wayCount should equal(1L)
      state.osmDistance should equal(196L)
      state.bounds should equal(Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458))
      state.osmSegments should equal(
        Seq(
          MonitorRouteSegment(
            1L,
            1001L,
            1002L,
            196L,
            Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
            """{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
          )
        )
      )
      state.matchesGeometry should equal(Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""))
      state.deviations should equal(Seq.empty)
      state.happy should equal(true)
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
    (config.monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(2022, 12, 1)), 1L).returns(Some(relation))
  }
}
