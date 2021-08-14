package kpn.core.database.views.monitor

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.MonitorAdminGroupRepositoryImpl
import kpn.server.repository.MonitorAdminRouteRepositoryImpl

class MonitorRouteViewTest extends UnitTest with SharedTestObjects {

  test("groups") {

    withDatabase { database =>

      val groupRepository = new MonitorAdminGroupRepositoryImpl(database)

      val group1 = newMonitorGroup("group-1", "Group one")
      val group2 = newMonitorGroup("group-2", "Group two")
      val group3 = newMonitorGroup("group-3", "Group three")

      groupRepository.saveGroup(group1)
      groupRepository.saveGroup(group2)
      groupRepository.saveGroup(group3)

      pending // no more couchdb database
      MonitorRouteView.groups(null) should matchTo(
        Seq(
          group1,
          group2,
          group3
        )
      )
    }
  }

  test("group routes") {

    withDatabase { database =>
      val routeRepository = new MonitorAdminRouteRepositoryImpl(database)

      val route1 = newMonitorRoute(101, "group-1", "Route one")
      val route2 = newMonitorRoute(102, "group-1", "Route two")
      val route3 = newMonitorRoute(103, "group-2", "Route three")

      routeRepository.saveRoute(route1)
      routeRepository.saveRoute(route2)
      routeRepository.saveRoute(route3)

      pending // no more couchdb database
      MonitorRouteView.groupRoutes(null, "group-1") should matchTo(Seq(route1, route2))
      MonitorRouteView.groupRoutes(null, "group-2") should matchTo(Seq(route3))
    }
  }

  test("all route ids") {

    withDatabase { database =>

      val routeRepository = new MonitorAdminRouteRepositoryImpl(database)

      val route1 = newMonitorRoute(101, "group-1", "Route one")
      val route2 = newMonitorRoute(102, "group-1", "Route two")
      val route3 = newMonitorRoute(103, "group-2", "Route three")

      routeRepository.saveRoute(route1)
      routeRepository.saveRoute(route2)
      routeRepository.saveRoute(route3)

      pending // no more couchdb database
      MonitorRouteView.allRouteIds(null) should equal(Seq(101L, 102L, 103L))
    }
  }
}
