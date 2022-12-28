package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.api.monitor.MonitorRelationDataBuilder
import kpn.server.api.monitor.domain.MonitorGroup
import org.scalamock.scalatest.MockFactory

import scala.xml.Elem
import scala.xml.XML

class MonitorUpdaterTest05 extends UnitTest with SharedTestObjects with MockFactory {

  test("add route with gpx references per sub-relation") {

    withDatabase() { database =>

      val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]

      val mainRelationData = OverpassData()
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

      val mainRelationStructure: Relation = new MonitorRelationDataBuilder(mainRelationData.rawData).data.relations(1L)

      (monitorRouteRelationRepository.loadStructure _).when(None, 1L).returns(Some(mainRelationStructure))

      val subRelationsOverpassData = OverpassData()
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

      val subRelationsData = new DataBuilder(subRelationsOverpassData.rawData).data
      val subRelation1 = subRelationsData.relations(11L)
      val subRelation2 = subRelationsData.relations(12L)

      (monitorRouteRelationRepository.loadTopLevel _).when(None, 11L).returns(Some(subRelation1))
      (monitorRouteRelationRepository.loadTopLevel _).when(None, 12L).returns(Some(subRelation2))

      val config = new MonitorUpdaterConfiguration(database, monitorRouteRelationRepository)
      val group = MonitorGroup(ObjectId(), "group", "")
      config.monitorGroupRepository.saveGroup(group)

      val properties = MonitorRouteProperties(
        group.name,
        "route-name",
        "route-description",
        Some("route-comment"),
        Some(1L),
        "multi-gpx",
        None,
        None,
        referenceFileChanged = false,
      )
      config.monitorUpdater.add("user", group.name, properties)

      val route = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.groupId should equal(group._id)
      route.name should equal("route-name")
      route.description should equal("route-description")
      route.comment should equal(Some("route-comment"))
      route.relationId should equal(Some(1L))
      route.user should equal("user")
      route.referenceType should equal("multi-gpx")
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
          monitorRouteRelation.relations.size should equal(2)
          val subRelation1 = monitorRouteRelation.relations.head
          subRelation1.relationId should equal(11L)
          subRelation1.name should equal(Some("sub-relation-1"))
          subRelation1.role should equal(None)
          subRelation1.survey should equal(None)
          subRelation1.deviationDistance should equal(0)
          subRelation1.deviationCount should equal(0)
          subRelation1.osmWayCount should equal(0)
          subRelation1.osmDistance should equal(0)
          subRelation1.osmSegmentCount should equal(0)
          subRelation1.happy should equal(false)
          subRelation1.relations.size should equal(0)

          val subRelation2 = monitorRouteRelation.relations(1)
          subRelation2.relationId should equal(12L)
          subRelation2.name should equal(Some("sub-relation-2"))
          subRelation2.role should equal(None) //: Option[String],
          subRelation2.survey should equal(None) //: Option[Day],
          subRelation2.deviationDistance should equal(0)
          subRelation2.deviationCount should equal(0)
          subRelation2.osmWayCount should equal(0)
          subRelation2.osmDistance should equal(0)
          subRelation2.osmSegmentCount should equal(0)
          subRelation1.happy should equal(false)
          subRelation2.relations.size should equal(0)
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

      val saveResult1 = config.monitorUpdater.upload(
        "user",
        group.name,
        route.name,
        11L,
        Day(2022, 12, 1),
        "filename-1",
        xml1
      )

      // saveResult1.analyzed should equal(true)

      val routeUpdated1 = config.monitorRouteRepository.routeByName(group._id, "route-name").get

      routeUpdated1.referenceDay should equal(None)
      routeUpdated1.referenceFilename should equal(None)
      routeUpdated1.referenceDistance should equal(196L)
      routeUpdated1.deviationDistance should equal(0L)
      routeUpdated1.deviationCount should equal(0L)
      routeUpdated1.osmWayCount should equal(1L)
      routeUpdated1.osmDistance should equal(196L)
      routeUpdated1.osmSegmentCount should equal(0L) // TODO overall osmSegmentCount still needs to be determined!
      routeUpdated1.happy should equal(false) // TODO happy cannot be determined until osmSegmentCount is calculated

      val reference1 = config.monitorRouteRepository.routeRelationReference(route._id, 11L).get
      reference1.routeId should equal(route._id)
      reference1.relationId should equal(Some(11L))
      reference1.distance should equal(196L)
      reference1.bounds should equal(Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458))
      reference1.segmentCount should equal(1)
      reference1.geometry should equal("""{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}""")

      val xml2: Elem = XML.loadString(
        """
          |<gpx>
          |  <trk>
          |    <trkseg>
          |      <trkpt lat="51.4618272" lon="4.4562458"></trkpt>
          |      <trkpt lat="51.4614496" lon="4.4550560"></trkpt>
          |    </trkseg>
          |  </trk>
          |</gpx>
          |""".stripMargin
      )

      val saveResult2 = config.monitorUpdater.upload(
        "user",
        group.name,
        route.name,
        12L,
        Day(2022, 12, 1),
        "filename-2",
        xml2
      )

      // saveResult2.analyzed should equal(true)

      val routeUpdated2 = config.monitorRouteRepository.routeByName(group._id, "route-name").get

      routeUpdated2.referenceDay should equal(None)
      routeUpdated2.referenceFilename should equal(None)
      routeUpdated2.referenceDistance should equal(335L)
      routeUpdated2.deviationDistance should equal(0L)
      routeUpdated2.deviationCount should equal(0L)
      routeUpdated2.osmWayCount should equal(2L)
      routeUpdated2.osmDistance should equal(335L)
      routeUpdated2.osmSegmentCount should equal(0L) // TODO overall osmSegmentCount still needs to be determined!
      routeUpdated2.happy should equal(false) // TODO happy cannot be determined until osmSegmentCount is calculated

      val reference2 = config.monitorRouteRepository.routeRelationReference(route._id, 12L).get
      reference2.routeId should equal(route._id)
      reference2.relationId should equal(Some(12L))
      reference2.distance should equal(139L)
      reference2.bounds should equal(Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458))
      reference2.segmentCount should equal(1)
      reference2.geometry should equal("""{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}""")
    }
  }
}
