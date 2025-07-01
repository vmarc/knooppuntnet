package kpn.database.actions.subsets

import kpn.api.common.Country
import kpn.api.common.LatLonImpl
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.subset.SubsetMapNetwork
import kpn.api.custom.Subset
import kpn.core.test.MongoTest

class MongoQuerySubsetMapNetworksTest extends MongoTest {

  test("subset map networks") {

    network(Country.nl, 1L, "network1", active = true)
    network(Country.nl, 2L, "network2", active = false)
    network(Country.be, 3L, "network3", active = true)

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

  private def network(country: Country, networkId: Long, name: String, active: Boolean): Unit = {
    database.networks.save(
      newNetworkDoc(
        _id = networkId,
        active = active,
        country = Some(country),
        summary = newNetworkSummary(
          name = name,
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
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
