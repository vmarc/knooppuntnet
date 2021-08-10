package kpn.core.mongo.actions.subsets

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NetworkRepositoryImpl

class MongoQuerySubsetNetworksTest extends UnitTest with SharedTestObjects {

  test("subset networks") {

    pending

    withDatabase { database =>

      val repository = new NetworkRepositoryImpl(database, null, true)

      // sorting order different from 'by network name'

      buildNetwork(repository, 1, "nl-rcn-2", Country.nl, NetworkType.cycling)
      buildNetwork(repository, 2, "be-rwn-2", Country.be, NetworkType.hiking)
      buildNetwork(repository, 3, "be-rwn-1", Country.be, NetworkType.hiking)
      buildNetwork(repository, 4, "nl-rcn-1", Country.nl, NetworkType.cycling)

      repository.networks(Subset.nlBicycle, stale = false) should matchTo(
        Seq(
          newNetworkAttributes(4, Some(Country.nl), NetworkType.cycling, name = "nl-rcn-1"),
          newNetworkAttributes(1, Some(Country.nl), NetworkType.cycling, name = "nl-rcn-2")
        )
      )

      repository.networks(Subset.beHiking, stale = false) should matchTo(
        Seq(
          newNetworkAttributes(3, Some(Country.be), NetworkType.hiking, name = "be-rwn-1"),
          newNetworkAttributes(2, Some(Country.be), NetworkType.hiking, name = "be-rwn-2")
        )
      )

      repository.networks(Subset.esHiking, stale = false) should equal(Seq.empty)
    }
  }

  private def buildNetwork(repository: NetworkRepository, id: Long, name: String, country: Country, networkType: NetworkType): Unit = {
    repository.saveNetworkInfo(
      newNetworkInfoDoc(
        id,
        country = Some(country),
        summary = newNetworkSummary(
          name = name,
          networkType
        )
      )
    )
  }
}
