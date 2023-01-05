package kpn.server.api.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSaveResult
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
import org.scalatest.BeforeAndAfterEach

import scala.xml.Elem
import scala.xml.XML

class MonitorUpdaterTest05 extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add route with gpx references per sub-relation") {

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
        "multi-gpx",
        None,
        None,
        referenceFileChanged = false,
      )

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      val saveResult = config.monitorUpdater.add("user", group.name, properties)
      saveResult should equal(MonitorRouteSaveResult())

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
          referenceType = "multi-gpx",
          referenceDay = None,
          referenceDistance = 0L,
          referenceFilename = None,
          deviationDistance = 0L,
          deviationCount = 0L,
          osmWayCount = 0L,
          osmDistance = 0L,
          osmSegmentCount = 0L,
          happy = false,
          superRouteOsmSegments = Seq.empty, // TODO ???
          relation = Some(
            MonitorRouteRelation(
              relationId = 1L,
              name = "main-relation",
              role = None,
              survey = None,
              deviationDistance = 0L,
              deviationCount = 0L,
              osmWayCount = 0L,
              osmDistance = 0L,
              osmSegmentCount = 0L,
              happy = false,
              relations = Seq(
                MonitorRouteRelation(
                  relationId = 11L,
                  name = "sub-relation-1",
                  role = None,
                  survey = None,
                  deviationDistance = 0L,
                  deviationCount = 0L,
                  osmWayCount = 0L,
                  osmDistance = 0L,
                  osmSegmentCount = 0L,
                  happy = false,
                  relations = Seq.empty
                ),
                MonitorRouteRelation(
                  relationId = 12L,
                  name = "sub-relation-2",
                  role = None,
                  survey = None,
                  deviationDistance = 0L,
                  deviationCount = 0L,
                  osmWayCount = 0L,
                  osmDistance = 0L,
                  osmSegmentCount = 0L,
                  happy = false,
                  relations = Seq.empty
                )
              )
            )
          )
        )
      )

      config.monitorRouteRepository.routeRelationReference(route._id, 1L) should equal(None)
      config.monitorRouteRepository.routeRelationReference(route._id, 11L) should equal(None)
      config.monitorRouteRepository.routeRelationReference(route._id, 12L) should equal(None)

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

      Time.set(Timestamp(2022, 8, 12, 12, 0, 0))
      val uploadSaveResult1 = config.monitorUpdater.upload(
        "user",
        group.name,
        route.name,
        11L,
        Day(2022, 8, 1),
        "filename-1",
        xml1
      )

      uploadSaveResult1 should equal(
        MonitorRouteSaveResult(
          analyzed = true
        )
      )

      val routeUpdated1 = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      routeUpdated1.shouldMatchTo(
        route.copy(
          referenceDay = None,
          referenceFilename = None,
          referenceDistance = 196L,
          deviationDistance = 0L,
          deviationCount = 0L,
          osmWayCount = 1L,
          osmDistance = 196L,
          osmSegmentCount = 0L, // TODO overall osmSegmentCount still needs to be determined!
          happy = false, // TODO happy cannot be determined until osmSegmentCount is calculated
          relation = route.relation.map { relation =>
            relation.copy(
              relations = Seq(
                relation.relations.head.copy(
                  osmWayCount = 1L,
                  osmDistance = 196L,
                  osmSegmentCount = 1L,
                  happy = true,
                ),
                relation.relations(1)
              )
            )
          }
        )
      )

      val reference1 = config.monitorRouteRepository.routeRelationReference(route._id, 11L).get
      reference1.shouldMatchTo(
        MonitorRouteReference(
          reference1._id,
          routeId = route._id,
          relationId = Some(11L),
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          user = "user",
          bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = "gpx", // TODO should be "multi-gpx" ???
          referenceDay = Day(2022, 8, 1),
          distance = 196L,
          segmentCount = 1L,
          filename = Some("filename-1"),
          geometry = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
        )
      )

      config.monitorRouteRepository.routeRelationReference(route._id, 1L) should equal(None)
      config.monitorRouteRepository.routeRelationReference(route._id, 12L) should equal(None)

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

      Time.set(Timestamp(2022, 8, 13, 12, 0, 0))
      val uploadSaveResult2 = config.monitorUpdater.upload(
        "user",
        group.name,
        route.name,
        12L,
        Day(2022, 8, 2),
        "filename-2",
        xml2
      )

      uploadSaveResult2 should equal(
        MonitorRouteSaveResult(
          analyzed = true
        )
      )

      val routeUpdated2 = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      routeUpdated2.shouldMatchTo(
        route.copy(
          referenceDay = None,
          referenceFilename = None,
          referenceDistance = 335L,
          deviationDistance = 0L,
          deviationCount = 0L,
          osmWayCount = 2L,
          osmDistance = 335L,
          osmSegmentCount = 0L, // TODO overall osmSegmentCount still needs to be determined!
          happy = false, // TODO happy cannot be determined until osmSegmentCount is calculated
          relation = route.relation.map { relation =>
            relation.copy(
              relations = Seq(
                relation.relations.head.copy(
                  osmWayCount = 1L,
                  osmDistance = 196L,
                  osmSegmentCount = 1L,
                  happy = true,
                ),
                relation.relations(1).copy(
                  osmWayCount = 1L,
                  osmDistance = 139L,
                  osmSegmentCount = 1L,
                  happy = true,
                )
              )
            )
          }
        )
      )

      config.monitorRouteRepository.routeRelationReference(route._id, 1L) should equal(None)
      config.monitorRouteRepository.routeRelationReference(route._id, 11L).shouldMatchTo(Some(reference1))

      val reference2 = config.monitorRouteRepository.routeRelationReference(route._id, 12L).get
      reference2.shouldMatchTo(
        MonitorRouteReference(
          reference2._id,
          routeId = route._id,
          relationId = Some(12L),
          timestamp = Timestamp(2022, 8, 13, 12, 0, 0),
          user = "user",
          bounds = Bounds(51.4614496, 4.455056, 51.4618272, 4.4562458),
          referenceType = "gpx", // TODO should be "multi-gpx" ???
          referenceDay = Day(2022, 8, 2),
          distance = 139L,
          segmentCount = 1,
          filename = Some("filename-2"),
          geometry = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
        )
      )
    }
  }

  private def setupLoadStructure(config: MonitorUpdaterConfiguration): Unit = {

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

    val relation = new MonitorRelationDataBuilder(overpassData.rawData).data.relations(1L)
    (config.monitorRouteRelationRepository.loadStructure _).when(None, 1L).returns(Some(relation))
  }

  private def setupLoadTopLevel(config: MonitorUpdaterConfiguration): Unit = {

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
    val subRelation1 = data.relations(11L)
    val subRelation2 = data.relations(12L)

    (config.monitorRouteRelationRepository.loadTopLevel _).when(None, 11L).returns(Some(subRelation1))
    (config.monitorRouteRelationRepository.loadTopLevel _).when(None, 12L).returns(Some(subRelation2))
  }
}
