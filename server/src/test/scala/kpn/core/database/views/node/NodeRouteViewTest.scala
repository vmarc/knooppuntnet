package kpn.core.database.views.node

import kpn.api.common.NodeRoute
import kpn.api.common.SharedTestObjects
import kpn.api.custom.ScopedNetworkType
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRouteRepositoryImpl

class NodeRouteViewTest extends UnitTest with SharedTestObjects {

  test("get all RouteDoc's for given networkType") {

    withDatabase { database =>

      val repo = new NodeRouteRepositoryImpl(database)

      repo.save(NodeRoute(1001, "01", ScopedNetworkType.rwn, Seq.empty, 2, 3))
      repo.save(NodeRoute(1002, "02", ScopedNetworkType.rwn, Seq.empty, 4, 5))
      repo.save(NodeRoute(1003, "03", ScopedNetworkType.rwn, Seq.empty, 6, 7))
      repo.save(NodeRoute(1004, "04", ScopedNetworkType.rcn, Seq.empty, 8, 9))
      repo.save(NodeRoute(1005, "05", ScopedNetworkType.lwn, Seq.empty, 9, 10))

      val nodeRoutes = NodeRouteView.query(database, ScopedNetworkType.rwn, stale = false)

      nodeRoutes should matchTo(
        Seq(
          NodeRoute(1001, "01", ScopedNetworkType.rwn, Seq.empty, 2, 3),
          NodeRoute(1002, "02", ScopedNetworkType.rwn, Seq.empty, 4, 5),
          NodeRoute(1003, "03", ScopedNetworkType.rwn, Seq.empty, 6, 7)
        )
      )
    }
  }
}
