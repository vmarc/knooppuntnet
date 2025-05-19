package kpn.database.actions.routes

import kpn.api.common.RouteScope.regional
import kpn.api.common.RouteType.hiking
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.core.doc.BaseNetworkDoc
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryRouteNetworkReferencesTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryRouteNetworkReferences(database)

      database.baseNetworks.save(buildNetwork(1L, "network-1", Seq(11L, 12L)))
      database.baseNetworks.save(buildNetwork(2L, "network-2", Seq(11L, 13L)))

      query.execute(11L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1", None),
          Reference(hiking, regional, 2L, "network-2", None)
        )
      )

      query.execute(12L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1", None)
        )
      )

      query.execute(13L) should equal(
        Seq(
          Reference(hiking, regional, 2L, "network-2", None)
        )
      )
    }
  }

  test("non-active networks are not included") {
    withDatabase { database =>
      val query = new MongoQueryRouteNetworkReferences(database)

      database.baseNetworks.save(buildNetwork(1L, "network-1", Seq(11L, 12L)))
      database.baseNetworks.save(buildNetwork(2L, "network-2", Seq(11L, 13L), active = false))

      query.execute(11L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1", None)
        )
      )
    }
  }

  private def buildNetwork(id: Long, name: String, routeIds: Seq[Long], active: Boolean = true): BaseNetworkDoc = {
    newBaseNetworkDoc(
      id,
      active = active,
      name = Some(name),
      routeType = hiking,
      routeScope = regional,
      members = routeIds.map(routeId =>
        RawMember(MemberType.Relation, routeId, None)
      )
    )
  }
}
