package kpn.database.actions.nodes

import kpn.api.common.NetworkScope
import kpn.api.common.NetworkScope.regional
import kpn.api.common.RouteType
import kpn.api.common.RouteType.hiking
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Reference
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryNodeNetworkReferencesTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      database.baseNetworks.save(
        newBaseNetworkDoc(
          1L,
          name = Some("network-1"),
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          nodeIds = Seq(1001L, 1002L)
        )
      )
      database.baseNetworks.save(
        newBaseNetworkDoc(2L,
          name = Some("network-2"),
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          nodeIds = Seq(1001L, 1003L)
        )
      )

      val query = new MongoQueryNodeNetworkReferences(database)
      query.execute(1001L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1"),
          Reference(hiking, regional, 2L, "network-2")
        )
      )
    }
  }

  test("non-active networks are not included") {
    withDatabase { database =>

      database.baseNetworks.save(
        newBaseNetworkDoc(
          1L,
          name = Some("network-1"),
          nodeIds = Seq(1001L, 1002L)
        )
      )
      database.baseNetworks.save(
        newBaseNetworkDoc(
          2L,
          active = false,
          name = Some("network-2"),
          nodeIds = Seq(1001L, 1003L),
        )
      )

      val query = new MongoQueryNodeNetworkReferences(database)
      query.execute(1001L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1")
        )
      )
    }
  }
}
