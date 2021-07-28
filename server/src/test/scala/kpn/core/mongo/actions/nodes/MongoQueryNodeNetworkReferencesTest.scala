package kpn.core.mongo.actions.nodes

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Reference
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.NetworkScope.regional
import kpn.api.custom.NetworkType.hiking
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryNodeNetworkReferencesTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryNodeNetworkReferences(database)

      database.oldNetworks.save(buildNetwork(1L, "network-1", Seq(1001L, 1002L)))
      database.oldNetworks.save(buildNetwork(2L, "network-2", Seq(1001L, 1003L)))

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
      val query = new MongoQueryNodeNetworkReferences(database)

      database.oldNetworks.save(buildNetwork(1L, "network-1", Seq(1001L, 1002L)))
      database.oldNetworks.save(buildNetwork(2L, "network-2", Seq(1001L, 1003L), active = false))

      query.execute(1001L) should equal(
        Seq(
          Reference(hiking, regional, 1L, "network-1")
        )
      )
    }
  }

  private def buildNetwork(id: Long, name: String, nodeRefs: Seq[Long], active: Boolean = true): NetworkInfo = {
    newNetworkInfo(
      newNetworkAttributes(
        id,
        networkType = hiking,
        networkScope = regional,
        name = name
      ),
      active = active,
      nodeRefs = nodeRefs
    )
  }
}
