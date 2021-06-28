package kpn.core.mongo.actions.routes

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Reference
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.NetworkScope.regional
import kpn.api.custom.NetworkType.hiking
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryRouteNetworkReferencesTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryRouteNetworkReferences(database)

      database.networks.save(buildNetwork(1L, "network-1", Seq(11L, 12L)))
      database.networks.save(buildNetwork(2L, "network-2", Seq(11L, 13L)))

      query.execute(11L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1"),
          Reference(hiking, regional, 2L, "network-2")
        )
      )

      query.execute(12L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1")
        )
      )

      query.execute(13L) should equal(
        Seq(
          Reference(hiking, regional, 2L, "network-2")
        )
      )
    }
  }

  test("non-active networks are not included") {
    withDatabase { database =>
      val query = new MongoQueryRouteNetworkReferences(database)

      database.networks.save(buildNetwork(1L, "network-1", Seq(11L, 12L)))
      database.networks.save(buildNetwork(2L, "network-2", Seq(11L, 13L), active = false))

      query.execute(11L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1")
        )
      )
    }
  }

  private def buildNetwork(id: Long, name: String, routeRefs: Seq[Long], active: Boolean = true): NetworkInfo = {
    newNetworkInfo(
      newNetworkAttributes(
        id,
        networkType = hiking,
        networkScope = regional,
        name = name
      ),
      active = active,
      routeRefs = routeRefs
    )
  }
}
