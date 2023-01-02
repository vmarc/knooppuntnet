package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
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
import kpn.server.api.monitor.domain.MonitorGroup
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest01 extends UnitTest with BeforeAndAfterEach with SharedTestObjects with MockFactory {

  override def beforeEach(): Unit = {
    Time.set(Timestamp(2023, 1, 1))
  }

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add simple route with osm reference") {

    withDatabase() { database =>

      val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]

      val mainRelationData = OverpassData()
        .relation(
          1,
          tags = Tags.from(
            "name" -> "route-name"
          ),
        )

      val relationStructure = new MonitorRelationDataBuilder(mainRelationData.rawData).data.relations(1L)

      (monitorRouteRelationRepository.loadStructure _).when(None, 1L).returns(Some(relationStructure))

      val detailOverpassData = OverpassData()
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

      val detailData = new DataBuilder(detailOverpassData.rawData).data
      val detailRelation = detailData.relations(1L)

      (monitorRouteRelationRepository.loadTopLevel _).when(None, 1L).returns(Some(detailRelation))
      (monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(2022, 12, 1)), 1L).returns(Some(detailRelation))

      val config = new MonitorUpdaterConfiguration(database, monitorRouteRelationRepository)
      val group = MonitorGroup(ObjectId(), "group", "")
      config.monitorGroupRepository.saveGroup(group)

      val properties = MonitorRouteProperties(
        group.name,
        "route-name",
        "route-description",
        Some("route-comment"),
        Some(1L),
        "osm",
        Some(Day(2022, 12, 1)),
        None,
        referenceFileChanged = false,
      )
      val saveResult = config.monitorUpdater.add("user", group.name, properties)
      // TODO saveResult.analyzed = true, no messages !

      val route = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.groupId should equal(group._id)
      route.name should equal("route-name")
      route.description should equal("route-description")
      route.comment should equal(Some("route-comment"))
      route.relationId should equal(Some(1L))
      route.user should equal("user")
      route.referenceType should equal("osm")
      route.referenceDay should equal(Some(Day(2022, 12, 1)))
      route.referenceFilename should equal(None)
      route.referenceDistance should equal(196L)
      route.deviationDistance should equal(0L)
      route.deviationCount should equal(0L)
      route.osmWayCount should equal(1L)
      route.osmDistance should equal(196L)
      route.osmSegmentCount should equal(1L)
      route.happy should equal(true)

      val relation = route.relation.get
      relation.relationId should equal(1L)
      relation.name should equal("route-name")
      relation.role should equal(None)
      relation.survey should equal(None)
      relation.deviationDistance should equal(0L)
      relation.deviationCount should equal(0L)
      relation.osmWayCount should equal(1L)
      relation.osmDistance should equal(196L)
      relation.osmSegmentCount should equal(1)
      relation.happy should equal(true)
      relation.relations.size should equal(0)

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
}
