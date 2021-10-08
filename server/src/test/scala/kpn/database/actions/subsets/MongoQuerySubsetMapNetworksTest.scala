package kpn.database.actions.subsets

import kpn.api.common.LatLonImpl
import kpn.api.common.SharedTestObjects
import kpn.api.common.subset.SubsetMapNetwork
import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database

class MongoQuerySubsetMapNetworksTest extends UnitTest with SharedTestObjects {

  test("subset map networks") {
    withDatabase { database =>

      network(database, Country.nl, 1L, "network1", active = true)
      network(database, Country.nl, 2L, "network2", active = false)
      network(database, Country.be, 3L, "network3", active = true)

      new MongoQuerySubsetMapNetworks(database).execute(Subset.nlHiking) should equal(
        Seq(
          SubsetMapNetwork(
            1L,
            "network1",
            101L,
            10,
            20,
            LatLonImpl("1", "1")
          )
        )
      )

      new MongoQuerySubsetMapNetworks(database).execute(Subset.beHiking) should equal(
        Seq(
          SubsetMapNetwork(
            3L,
            "network3",
            103L,
            30,
            60,
            LatLonImpl("3", "3")
          )
        )
      )
    }
  }

  private def network(database: Database, country: Country, networkId: Long, name: String, active: Boolean): Unit = {
    database.networkInfos.save(
      newNetworkInfoDoc(
        _id = networkId,
        active = active,
        country = Some(country),
        summary = newNetworkSummary(
          name = name,
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          nodeCount = networkId * 10,
          routeCount = networkId * 20,
        ),
        detail = newNetworkDetail(
          km = 100 + networkId,
          center = Some(LatLonImpl(networkId.toString, networkId.toString))
        ),
      )
    )
  }
}
