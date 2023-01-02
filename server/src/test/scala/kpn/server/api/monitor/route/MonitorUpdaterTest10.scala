package kpn.server.api.monitor.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterEach

class MonitorUpdaterTest10 extends UnitTest with BeforeAndAfterEach with SharedTestObjects with MockFactory {

  override def beforeEach(): Unit = {
    Time.set(Timestamp(2023, 1, 1))
  }

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route update - no changes") {

    withDatabase() { database =>

      val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
      val config = new MonitorUpdaterConfiguration(database, monitorRouteRelationRepository)

      val group = newMonitorGroup("group")
      val route = newMonitorRoute(
        group._id,
        name = "route",
        relationId = Some(1L),
        user = "user",
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFilename = None,
      )
      val reference = newMonitorRouteReference(
        routeId = route._id,
        relationId = route.relationId,
        referenceType = "osm",
        referenceDay = Day(2022, 8, 11),
      )
      val state = newMonitorRouteState(
        route._id,
        1L,
        timestamp = Timestamp(2022, 8, 11),
      )

      config.monitorGroupRepository.saveGroup(group)
      config.monitorRouteRepository.saveRoute(route)
      config.monitorRouteRepository.saveRouteReference(reference)
      config.monitorRouteRepository.saveRouteState(state)

      val properties = MonitorRouteProperties(
        groupName = group.name,
        name = "route",
        description = "",
        comment = None,
        relationId = Some(1L),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFilename = None,
        referenceFileChanged = false,
      )

      val saveResult = config.monitorUpdater.update("user", "group", "route", properties)

      saveResult should equal(MonitorRouteSaveResult())

      val updatedRoute = config.monitorRouteRepository.routeByName(group._id, "route").get
      val updatedReference = config.monitorRouteRepository.routeRelationReference(route._id, 1L).get
      val updatedState = config.monitorRouteRepository.routeState(route._id, 1L).get

      updatedRoute should equal(route)
      updatedReference should equal(reference)
      updatedState should equal(state)
    }
  }
}
