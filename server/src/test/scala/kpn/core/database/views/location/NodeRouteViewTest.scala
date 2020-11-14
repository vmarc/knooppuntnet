package kpn.core.database.views.location

import kpn.api.common.NodeRoute
import kpn.api.common.SharedTestObjects
import kpn.api.custom.NetworkType
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRouteRepositoryImpl

class NodeRouteViewTest extends UnitTest with SharedTestObjects {

  test("get all RouteDoc's for given networkType") {

    withDatabase { database =>

      val repo = new NodeRouteRepositoryImpl(database)

      repo.save(NodeRoute(1001, NetworkType.hiking, 2, Some(3)))
      repo.save(NodeRoute(1002, NetworkType.hiking, 4, Some(5)))
      repo.save(NodeRoute(1003, NetworkType.hiking, 6, None))
      repo.save(NodeRoute(1004, NetworkType.cycling, 8, Some(9)))

      val nodeRoutes = NodeRouteView.query(database, NetworkType.hiking, stale = false)

      nodeRoutes should equal(
        Seq(
          NodeRoute(1001, NetworkType.hiking, 2, Some(3)),
          NodeRoute(1002, NetworkType.hiking, 4, Some(5)),
          NodeRoute(1003, NetworkType.hiking, 6, None)
        )
      )
    }
  }

}
