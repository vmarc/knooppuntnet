package kpn.database.actions.routes

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Reference
import kpn.api.custom.NetworkScope.regional
import kpn.api.custom.NetworkType.hiking
import kpn.core.doc.NetworkInfoDoc
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryRouteNetworkReferencesTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryRouteNetworkReferences(database)

      database.networkInfos.save(buildNetwork(1L, "network-1", Seq(11L, 12L)))
      database.networkInfos.save(buildNetwork(2L, "network-2", Seq(11L, 13L)))

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

      database.networkInfos.save(buildNetwork(1L, "network-1", Seq(11L, 12L)))
      database.networkInfos.save(buildNetwork(2L, "network-2", Seq(11L, 13L), active = false))

      query.execute(11L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1")
        )
      )
    }
  }

  private def buildNetwork(id: Long, name: String, routeIds: Seq[Long], active: Boolean = true): NetworkInfoDoc = {
    newNetworkInfoDoc(
      id,
      active = active,
      summary = newNetworkSummary(
        name = name,
        networkType = hiking,
        networkScope = regional
      ),
      routes = routeIds.map(routeId =>
        newNetworkRouteRow(routeId)
      )
    )
  }
}
