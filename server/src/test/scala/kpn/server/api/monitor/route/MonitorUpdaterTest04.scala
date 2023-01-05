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

import scala.xml.XML

class MonitorUpdaterTest04 extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add non-super route with single gpx reference") {

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
        "gpx",
        None,
        None,
        referenceFileChanged = false,
      )

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      val addSaveResult = config.monitorUpdater.add("user", group.name, properties)
      addSaveResult should equal(MonitorRouteSaveResult())

      val route = config.monitorRouteRepository.routeByName(group._id, "route-name").get
      route.shouldMatchTo(
        MonitorRoute(
          route._id,
          groupId = group._id,
          name = "route-name",
          description = "route-description",
          comment = Some("route-comment"),
          relationId = Some(1L),
          user = "user",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          referenceType = "gpx",
          referenceDay = None,
          referenceFilename = None,
          referenceDistance = 0L,
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
              name = "route-name",
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

      config.monitorRouteRepository.routeRelationReference(route._id, 1L) should equal(None)

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
      val uploadSaveResult = config.monitorUpdater.upload(
        "user2",
        group.name,
        route.name,
        1L,
        Day(2022, 8, 1),
        "filename",
        xml1
      )

      uploadSaveResult should equal(
        MonitorRouteSaveResult(
          analyzed = true
        )
      )

      config.monitorRouteRepository.routeByName(group._id, "route-name").shouldMatchTo(
        Some(
          route.copy(
            referenceDay = Some(Day(2022, 8, 1)),
            referenceFilename = Some("filename"),
            referenceDistance = 196L,
            deviationDistance = 0L,
            deviationCount = 0L,
            osmWayCount = 1L,
            osmDistance = 196L,
            osmSegmentCount = 1L,
            happy = true,
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
      )

      val reference = config.monitorRouteRepository.routeRelationReference(route._id, 1L).get
      reference.shouldMatchTo(
        MonitorRouteReference(
          reference._id,
          routeId = route._id,
          relationId = Some(1L),
          timestamp = Timestamp(2022, 8, 12, 12, 0, 0),
          user = "user2",
          bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
          referenceType = "gpx",
          referenceDay = Day(2022, 8, 1),
          distance = 196L,
          segmentCount = 1L,
          filename = Some("filename"),
          geometry = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
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
        )
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
  }
}
