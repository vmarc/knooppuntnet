package kpn.core.database.views.monitor

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.MonitorAdminGroupRepositoryImpl
import kpn.server.repository.MonitorAdminRouteRepositoryImpl

class MonitorRouteViewTest extends UnitTest with SharedTestObjects {

  test("groups") {

    withCouchDatabase { database =>

      val groupRepository = new MonitorAdminGroupRepositoryImpl(null, database, false)

      val group1 = newMonitorGroup("group-1", "Group one")
      val group2 = newMonitorGroup("group-2", "Group two")
      val group3 = newMonitorGroup("group-3", "Group three")

      groupRepository.saveGroup(group1)
      groupRepository.saveGroup(group2)
      groupRepository.saveGroup(group3)

      MonitorRouteView.groups(database, stale = false) should matchTo(
        Seq(
          group1,
          group2,
          group3
        )
      )
    }
  }

  test("group routes") {

    withCouchDatabase { database =>
      val routeRepository = new MonitorAdminRouteRepositoryImpl(null, database, false)

      val route1 = newMonitorRoute(101, "group-1", "Route one")
      val route2 = newMonitorRoute(102, "group-1", "Route two")
      val route3 = newMonitorRoute(103, "group-2", "Route three")

      routeRepository.saveRoute(route1)
      routeRepository.saveRoute(route2)
      routeRepository.saveRoute(route3)

      MonitorRouteView.groupRoutes(database, "group-1", stale = false) should matchTo(Seq(route1, route2))
      MonitorRouteView.groupRoutes(database, "group-2", stale = false) should matchTo(Seq(route3))
    }
  }

  test("all route ids") {

    withCouchDatabase { database =>

      val routeRepository = new MonitorAdminRouteRepositoryImpl(null, database, false)

      val route1 = newMonitorRoute(101, "group-1", "Route one")
      val route2 = newMonitorRoute(102, "group-1", "Route two")
      val route3 = newMonitorRoute(103, "group-2", "Route three")

      routeRepository.saveRoute(route1)
      routeRepository.saveRoute(route2)
      routeRepository.saveRoute(route3)

      MonitorRouteView.allRouteIds(database, stale = false) should equal(Seq(101L, 102L, 103L))
    }
  }
}
