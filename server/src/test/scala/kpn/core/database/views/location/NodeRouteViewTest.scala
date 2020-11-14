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

      repo.save(NodeRoute(1001, "01", NetworkType.hiking, Seq(), 2, 3))
      repo.save(NodeRoute(1002, "02", NetworkType.hiking, Seq(), 4, 5))
      repo.save(NodeRoute(1003, "03", NetworkType.hiking, Seq(), 6, 7))
      repo.save(NodeRoute(1004, "04", NetworkType.cycling, Seq(), 8, 9))

      val nodeRoutes = NodeRouteView.query(database, NetworkType.hiking, stale = false)

      nodeRoutes should equal(
        Seq(
          NodeRoute(1001, "01", NetworkType.hiking, Seq(), 2, 3),
          NodeRoute(1002, "02", NetworkType.hiking, Seq(), 4, 5),
          NodeRoute(1003, "03", NetworkType.hiking, Seq(), 6, 7)
        )
      )
    }
  }

}
