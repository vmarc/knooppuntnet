package kpn.core.database.views.node

import kpn.api.common.NodeRoute
import kpn.api.common.SharedTestObjects
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRouteRepositoryImpl

class NodeRouteViewTest extends UnitTest with SharedTestObjects {

  test("get all RouteDoc's for given networkType") {

    withCouchDatabase { database =>

      val repo = new NodeRouteRepositoryImpl(database)

      repo.save(NodeRoute(1001, "01", NetworkType.hiking, NetworkScope.regional, Seq.empty, 2, 3))
      repo.save(NodeRoute(1002, "02", NetworkType.hiking, NetworkScope.regional, Seq.empty, 4, 5))
      repo.save(NodeRoute(1003, "03", NetworkType.hiking, NetworkScope.regional, Seq.empty, 6, 7))
      repo.save(NodeRoute(1004, "04", NetworkType.cycling, NetworkScope.regional, Seq.empty, 8, 9))
      repo.save(NodeRoute(1005, "05", NetworkType.hiking, NetworkScope.local, Seq.empty, 9, 10))

      val nodeRoutes = NodeRouteView.query(database, ScopedNetworkType.rwn, stale = false)

      nodeRoutes should matchTo(
        Seq(
          NodeRoute(1001, "01", NetworkType.hiking, NetworkScope.regional, Seq.empty, 2, 3),
          NodeRoute(1002, "02", NetworkType.hiking, NetworkScope.regional, Seq.empty, 4, 5),
          NodeRoute(1003, "03", NetworkType.hiking, NetworkScope.regional, Seq.empty, 6, 7)
        )
      )
    }
  }
}
