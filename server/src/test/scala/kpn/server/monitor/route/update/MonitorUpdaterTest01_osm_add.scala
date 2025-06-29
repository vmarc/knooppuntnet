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

class MonitorUpdaterTest01_osm_add extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val log = new MockLog()

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("add non-super route with osm reference") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)
      setupLoadStructure(configuration)
      setupLoadTopLevel(configuration)
      setupBaseRouteDoc(configuration)

      val group = newMonitorGroup("group")
      configuration.monitorGroupRepository.saveGroup(group)

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
            referenceType = MonitorReferenceType.osm,
            description = Some("route-description"),
            comment = Some("route-comment"),
            relationId = Some(1),
            referenceTimestamp = Some(Timestamp(2022, 8, 1)),
          )
        )
      )

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(1)

      val monitorRoute = assertMonitorRoute(configuration, group)
      assertRouteReference(configuration, monitorRoute)
      assertRouteState(configuration, monitorRoute)
      assertMessages(reporter)
    }
  }

  private def assertMonitorRoute(configuration: MonitorUpdaterConfiguration, group: MonitorGroup) = {
    val route = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    assertEqual(
      route.copy(analysisDuration = None),
      MonitorRoute(
        _id = route._id,
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
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Some(Timestamp(2022, 8, 1)),
        referenceFilename = None,
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
    route
  }

  private def assertRouteReference(configuration: MonitorUpdaterConfiguration, monitorRoute: MonitorRoute): Unit = {
    val reference = configuration.monitorRouteRepository.routeReference(monitorRoute._id, Some(1)).get
    assertEqual(
      reference,
      MonitorRouteReference(
        _id = reference._id,
        routeId = monitorRoute._id,
        relationId = Some(1),
        timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
        user = "user",
        referenceBounds = Bounds(51.4618272, 4.4553911, 51.4633666, 4.4562458),
        referenceType = MonitorReferenceType.osm,
        referenceTimestamp = Timestamp(2022, 8, 1),
        referenceDistance = 181,
        referenceSegmentCount = 1,
        referenceFilename = None,
        referenceGeoJson = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}]}"""
      )
    )
  }

  private def assertRouteState(configuration: MonitorUpdaterConfiguration, monitorRoute: MonitorRoute): Unit = {
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
            MonitorRouteUpdateStatusCommand("step-add", "1", Some("1/1 route-name")),
            MonitorRouteUpdateStatusCommand("step-add", "save"))
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "1")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "save")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-done", "save")
          )
        )
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
        ),
      )

    setupRouteStructure(configuration, overpassData, 1)
  }

  private def setupLoadTopLevel(configuration: MonitorUpdaterConfiguration): Unit = {

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
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(2022, 8, 1)), 1).returns(Some(relation))
  }
}
