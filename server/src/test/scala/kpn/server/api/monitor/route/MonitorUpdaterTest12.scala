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

class MonitorUpdaterTest12 extends UnitTest with BeforeAndAfterEach with SharedTestObjects with MockFactory {

  override def beforeEach(): Unit = {
    Time.set(Timestamp(2023, 1, 1))
  }

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("route update - change group") {

    withDatabase() { database =>

      val monitorRouteRelationRepository = stub[MonitorRouteRelationRepository]
      val config = new MonitorUpdaterConfiguration(database, monitorRouteRelationRepository)

      val group1 = newMonitorGroup("group1", "")
      val group2 = newMonitorGroup("group2", "")
      val route = newMonitorRoute(
        group1._id,
        name = "route",
        relationId = Some(1L),
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

      config.monitorGroupRepository.saveGroup(group1)
      config.monitorGroupRepository.saveGroup(group2)
      config.monitorRouteRepository.saveRoute(route)
      config.monitorRouteRepository.saveRouteReference(reference)

      val properties = MonitorRouteProperties(
        groupName = group2.name, // <-- changed
        name = "route",
        description = "",
        comment = None,
        relationId = Some(1L),
        referenceType = "osm",
        referenceDay = Some(Day(2022, 8, Some(11))),
        referenceFilename = None,
        referenceFileChanged = false,
      )

      val saveResult = config.monitorUpdater.update("user", "group1", "route", properties)

      saveResult should equal(MonitorRouteSaveResult())

      val updatedRoute = config.monitorRouteRepository.routeByName(group2._id, "route").get
      val updatedReference = config.monitorRouteRepository.routeRelationReference(route._id, 1L).get
      // TODO val updatedState = config.monitorRouteRepository.routeState(route._id, 1L).get

      updatedRoute.groupId should equal(group2._id)
      updatedReference should equal(reference)
      // TODO assert state not updated
    }
  }
}
