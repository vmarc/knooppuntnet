package kpn.database.actions.nodes

import kpn.api.common.RouteScope
import kpn.api.common.RouteScope.regional
import kpn.api.common.RouteType
import kpn.api.common.RouteType.hiking
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.core.test.SharedTestObjects
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
          routeScope = RouteScope.regional,
          members = Seq(
            RawMember(MemberType.Node, 1001L, Some("connection")),
            RawMember(MemberType.Node, 1002L, None),
          )
        )
      )
      database.baseNetworks.save(
        newBaseNetworkDoc(2L,
          name = Some("network-2"),
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          members = Seq(
            RawMember(MemberType.Node, 1001L, None),
            RawMember(MemberType.Node, 1003L, None),
          )
        )
      )

      val query = new MongoQueryNodeNetworkReferences(database)
      query.execute(1001L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1", Some("connection")),
          Reference(hiking, regional, 2L, "network-2", None)
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
          members = Seq(
            RawMember(MemberType.Node, 1001L, None),
            RawMember(MemberType.Node, 1002L, None),
          )
        )
      )
      database.baseNetworks.save(
        newBaseNetworkDoc(
          2L,
          active = false,
          name = Some("network-2"),
          members = Seq(
            RawMember(MemberType.Node, 1001L, None),
            RawMember(MemberType.Node, 1003L, None),
          )
        )
      )

      val query = new MongoQueryNodeNetworkReferences(database)
      query.execute(1001L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1", None)
        )
      )
    }
  }
}
