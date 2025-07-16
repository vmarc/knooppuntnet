package kpn.database.actions.monitor

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newMonitorGroup
import kpn.core.test.TestObjects.newMonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl

class MongoQueryMonitorRouteInfosTest extends MongoTest {

  test("all route infos") {

    setupRoutes()

    val routeInfos = new MongoQueryMonitorRouteInfos(database).execute()

    assertEqual(
      routeInfos.map(routeInfo => (routeInfo.groupName, routeInfo.routeName)),
      Seq(
        ("group1", "route11"),
        ("group1", "route12"),
        ("group2", "route21"),
        ("group2", "route22"),
      )
    )
  }

  private def setupRoutes(): Unit = {
    val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
    val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)

    val group1 = newMonitorGroup("group1")
    val group2 = newMonitorGroup("group2")

    val route11 = newMonitorRoute(group1._id, "route11")
    val route12 = newMonitorRoute(group1._id, "route12")
    val route21 = newMonitorRoute(group2._id, "route21")
    val route22 = newMonitorRoute(group2._id, "route22")

    monitorGroupRepository.saveGroup(group1)
    monitorGroupRepository.saveGroup(group2)
    monitorRouteRepository.saveRoute(route11)
    monitorRouteRepository.saveRoute(route12)
    monitorRouteRepository.saveRoute(route21)
    monitorRouteRepository.saveRoute(route22)
  }
}
