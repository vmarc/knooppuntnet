package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.api.monitor.MonitorRelationDataBuilder
import kpn.server.api.monitor.domain.MonitorGroup
import org.scalamock.scalatest.MockFactory

import scala.xml.XML

class MonitorUpdaterTest04 extends UnitTest with SharedTestObjects with MockFactory {

  test("add non-super route with single gpx reference") {

    withDatabase() { database =>

      val config = new MonitorUpdaterConfiguration(database)

      val structureOverpassData = OverpassData()
        .relation(
          1,
          tags = Tags.from(
            "name" -> "route-name"
          )
        )

      val relationStructure = new MonitorRelationDataBuilder(structureOverpassData.rawData).data.relations(1L)

      (config.monitorRouteRelationRepository.loadStructure _).when(None, 1L).returns(Some(relationStructure))

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

      val relationData = new DataBuilder(overpassData.rawData).data
      val relation = relationData.relations(1L)

      (config.monitorRouteRelationRepository.loadTopLevel _).when(None, 1L).returns(Some(relation))

      val group = MonitorGroup(ObjectId(), "group", "")
      config.monitorGroupRepository.saveGroup(group)

      val properties = MonitorRouteProperties(
        group.name,
        "route-name",
        "route-description",
        Some("route-comment"),
        Some(1L),
        "gpx",
        None,
        None,
        referenceFileChanged = false,
      )

      val addSaveResult = config.monitorUpdater.add("user", group.name, properties)
      addSaveResult should equal(
        MonitorRouteSaveResult()
      )

      val route = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.groupId should equal(group._id)
      route.name should equal("route-name")
      route.description should equal("route-description")
      route.comment should equal(Some("route-comment"))
      route.relationId should equal(Some(1L))
      route.user should equal("user")
      route.referenceType should equal("gpx")
      route.referenceDay should equal(None)
      route.referenceFilename should equal(None)
      route.referenceDistance should equal(0)
      route.deviationDistance should equal(0)
      route.deviationCount should equal(0)
      route.osmWayCount should equal(0)
      route.osmDistance should equal(0)
      route.osmSegmentCount should equal(0)
      route.happy should equal(false)

      route.relation match {
        case None => fail("MonitorRouteRelation not found")
        case Some(monitorRouteRelation) =>
          monitorRouteRelation.relations.size should equal(0)
          monitorRouteRelation.relationId should equal(1L)
          monitorRouteRelation.name should equal("route-name")
          monitorRouteRelation.role should equal(None)
          monitorRouteRelation.survey should equal(None)
          monitorRouteRelation.deviationDistance should equal(0)
          monitorRouteRelation.deviationCount should equal(0)
          monitorRouteRelation.osmWayCount should equal(0)
          monitorRouteRelation.osmDistance should equal(0)
          monitorRouteRelation.osmSegmentCount should equal(0)
          monitorRouteRelation.happy should equal(false)
          monitorRouteRelation.relations.size should equal(0)

      }

      // TODO verify that reference does not exist in the database yet?

      val xml1 = XML.loadString(
        """
          |<gpx>
          |  <trk>
          |    <trkseg>
          |      <trkpt lat="51.4633666" lon="4.4553911"></trkpt>
          |      <trkpt lat="51.4618272" lon="4.4562458"></trkpt>
          |    </trkseg>
          |  </trk>
          |</gpx>
          |""".stripMargin
      )

      val uploadSaveResult = config.monitorUpdater.upload(
        "user",
        group.name,
        route.name,
        1L,
        Day(2022, 12, 1),
        "filename-1",
        xml1
      )

      uploadSaveResult should equal(
        MonitorRouteSaveResult(
          analyzed = true
        )
      )

      val updatedRoute = config.monitorRouteRepository.routeByName(group._id, "route-name").get

      updatedRoute.referenceDay should equal(Some(Day(2022, 12, 1)))
      updatedRoute.referenceFilename should equal(Some("filename-1"))
      updatedRoute.referenceDistance should equal(196L)
      updatedRoute.deviationDistance should equal(0L)
      updatedRoute.deviationCount should equal(0L)
      updatedRoute.osmWayCount should equal(1L)
      updatedRoute.osmDistance should equal(196L)
      updatedRoute.osmSegmentCount should equal(1L)
      updatedRoute.happy should equal(true)

      val reference = config.monitorRouteRepository.routeRelationReference(route._id, 1L).get
      reference.routeId should equal(route._id)
      reference.relationId should equal(Some(1L))
      reference.distance should equal(196L)
      reference.bounds should equal(Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458))
      reference.segmentCount should equal(1)
      reference.geometry should equal("""{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}""")
    }
  }
}
