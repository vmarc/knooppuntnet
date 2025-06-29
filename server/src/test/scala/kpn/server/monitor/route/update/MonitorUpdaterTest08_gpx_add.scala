package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest08_gpx_add extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val log = new MockLog()

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add non-super route with single gpx reference") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)
      setupLoadStructure(configuration)
      setupLoadRelation(configuration)
      setupBaseRouteDoc(configuration)

      val group = newMonitorGroup("group")
      configuration.monitorGroupRepository.saveGroup(group)

      val gpx =
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

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user",
          reporter,
          MonitorRouteUpdate(
            action = MonitorAction.add,
            groupName = group.name,
            routeName = "route-name",
            referenceType = MonitorReferenceType.gpx,
            description = Some("route-description"),
            comment = Some("route-comment"),
            relationId = Some(1),
            referenceTimestamp = Some(Timestamp(2022, 8, 1)),
            referenceFilename = Some("filename"),
            referenceGpx = Some(gpx)
          )
        )
      )

      assertMessages(reporter)

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(1)

      val route = assertRoute(configuration, group)
      assertReference(configuration, route)
      assertState(configuration, route)
    }
  }

  private def assertRoute(configuration: MonitorUpdaterConfiguration, group: MonitorGroup) = {
    val monitorRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      monitorRoute.copy(analysisDuration = None),
      MonitorRoute(
        monitorRoute._id,
        groupId = group._id,
        name = "route-name",
        description = "route-description",
        comment = Some("route-comment"),
        relationId = Some(1),
        user = "user",
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        symbol = None,
        analysisTimestamp = Some(Timestamp(2022, 8, 11, 12, 0, 0)),
        analysisDuration = None,
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = Some(Timestamp(2022, 8, 1)),
        referenceFilename = Some("filename"),
        referenceDistance = 181,
        deviationDistance = 0,
        deviationCount = 0,
        osmSegmentCount = 1,
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
            name = "route-name",
            happy = true,
          )
        ),
        happy = true
      )
    )
    monitorRoute
  }

  private def assertReference(configuration: MonitorUpdaterConfiguration, monitorRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(monitorRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        reference._id,
        routeId = monitorRoute._id,
        relationId = Some(1),
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        user = "user",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.gpx,
        referenceTimestamp = Timestamp(2022, 8, 1),
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = Some("filename"),
        referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
      )
    )
  }

  private def assertState(configuration: MonitorUpdaterConfiguration, monitorRoute: MonitorRoute): Unit = {
    val state = configuration.monitorRouteRepository.routeState(monitorRoute._id, 1).get
    assertEqual(
      state,
      MonitorRouteState(
        state._id,
        routeId = monitorRoute._id,
        relationId = 1,
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        // TODO redesign cleanup - bounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        matchesGeometry = Some("""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""),
        deviations = Seq.empty,
      )
    )
  }

  private def setupBaseRouteDoc(configuration: MonitorUpdaterConfiguration): Unit = {
    configuration.routeRepository.saveBaseRoute(
      newBaseRouteDoc(
        newRouteSummary(1),
        segments = Seq(
          newBaseRouteSegment(1)
        ),
        segmentElements = Seq(
          newBaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            coordinates = "[[4.4553911, 51.4633666],[4.4562458,51.4618272]]"
          )
        )
      )
    )
  }

  private def setupLoadStructure(configuration: MonitorUpdaterConfiguration): Unit = {
    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route-name"
        )
      )
    setupRouteStructure(configuration, overpassData, 1)
  }

  private def setupLoadRelation(configuration: MonitorUpdaterConfiguration): Unit = {

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
          newMember(MemberType.Way, 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.load _).when(None, 1).returns(Some(relation))
  }

  private def assertMessages(reporter: MonitorUpdateReporterMock): Unit = {
    assertEqual(
      reporter.messages,
      Seq(
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-add", "prepare"),
            MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
            MonitorRouteUpdateStatusCommand("step-active", "prepare")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "analyze-route-structure")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-add", "load-gpx"),
            MonitorRouteUpdateStatusCommand("step-add", "analyze"),
            MonitorRouteUpdateStatusCommand("step-active", "load-gpx")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "analyze")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "save")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-done", "save"))
        )
      )
    )
  }
}
