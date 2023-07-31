package kpn.server.monitor.route.update

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.MockLog
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRoute
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest04_osm_update extends UnitTest with BeforeAndAfterEach with SharedTestObjects {

  private val log = new MockLog()

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route with osm reference - delete previous reference if route not found at referenceTimestamp") {

    withDatabase() { database =>

      val configuration = MonitorUpdaterTestSupport.configuration(database)
      setupStructureLoader(configuration)
      setupLoadTopLevel(configuration)

      val group = newMonitorGroup("group")
      configuration.monitorGroupRepository.saveGroup(group)

      val route = newMonitorRoute(
        group._id,
        name = "route",
        relationId = Some(1),
        user = "user",
        referenceType = "osm",
        referenceTimestamp = Some(Timestamp(2022, 8, 11)),
        referenceFilename = None,
        relation = Some(
          newMonitorRouteRelation(
            relationId = 1,
            name = "route"
          )
        )
      )
      val reference = newMonitorRouteReference(
        routeId = route._id,
        relationId = Some(1),
        referenceType = "osm",
        referenceTimestamp = Timestamp(2022, 8, 11),
      )
      val state = newMonitorRouteState(
        route._id,
        1,
        timestamp = Timestamp(2022, 8, 11),
      )

      configuration.monitorGroupRepository.saveGroup(group)
      configuration.monitorRouteRepository.saveRoute(route)
      configuration.monitorRouteRepository.saveRouteReference(reference)
      configuration.monitorRouteRepository.saveRouteState(state)

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(1)
      database.monitorRouteStates.countDocuments(log) should equal(1)

      Time.set(Timestamp(2022, 8, 11, 12, 0, 0))
      val reporter = new MonitorUpdateReporterMock()
      configuration.monitorRouteUpdateExecutor.execute(
        MonitorUpdateContext(
          "user",
          reporter,
          MonitorRouteUpdate(
            action = "update",
            groupName = group.name,
            routeName = "route",
            referenceType = "osm",
            description = Some("route description"),
            relationId = Some(1),
            referenceTimestamp = Some(Timestamp(2022, 8, 1)),
          )
        )
      )

      assertMessages(reporter)

      database.monitorRoutes.countDocuments(log) should equal(1)
      database.monitorRouteReferences.countDocuments(log) should equal(0)
      database.monitorRouteStates.countDocuments(log) should equal(0)

      val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route").get
      updatedRoute.shouldMatchTo(
        MonitorRoute(
          _id = route._id,
          groupId = group._id,
          name = "route",
          description = "route description",
          comment = None,
          relationId = Some(1),
          user = "user",
          timestamp = Timestamp(2022, 8, 11, 12, 0, 0),
          symbol = None,
          referenceType = "osm",
          referenceTimestamp = Some(Timestamp(2022, 8, 1)),
          referenceFilename = None,
          referenceDistance = 0,
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 0,
          osmDistance = 0,
          osmSegmentCount = 0,
          osmSegments = Seq.empty,
          relation = Some(
            MonitorRouteRelation(
              relationId = 1,
              name = "route",
              role = None,
              survey = None,
              symbol = None,
              referenceTimestamp = None,
              referenceFilename = None,
              referenceDistance = 0,
              deviationDistance = 0,
              deviationCount = 0,
              osmWayCount = 0,
              osmSegmentCount = 0,
              osmDistance = 0,
              osmDistanceSubRelations = 0,
              gaps = None,
              happy = false,
              relations = Seq.empty
            )
          ),
          happy = false
        )
      )

      configuration.monitorRouteRepository.routeReference(route._id, Some(1)) should equal(None)
      configuration.monitorRouteRepository.routeState(route._id, 1) should equal(None)
    }
  }

  private def setupStructureLoader(configuration: MonitorUpdaterConfiguration): Unit = {

    val overpassData = OverpassData()
      .relation(
        1,
        tags = Tags.from(
          "name" -> "route"
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
          "name" -> "route"
        ),
        members = Seq(
          newMember("way", 101),
        )
      )

    val relation = new DataBuilder(overpassData.rawData).data.relations(1)
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(None, 1).returns(Some(relation))
    (configuration.monitorRouteRelationRepository.loadTopLevel _).when(Some(Timestamp(2022, 8, 1)), 1).returns(None)
  }

  private def assertMessages(reporter: MonitorUpdateReporterMock): Unit = {
    reporter.messages.shouldMatchTo(
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
            MonitorRouteUpdateStatusCommand("step-add", "1", Some("1/1 route")),
            MonitorRouteUpdateStatusCommand("step-add", "save")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-active", "1")
          )
        ),
        MonitorRouteUpdateStatusMessage(
          errors = Some(Seq("Could not load relation 1 at 2022-08-01 00:00:00"))
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
}
