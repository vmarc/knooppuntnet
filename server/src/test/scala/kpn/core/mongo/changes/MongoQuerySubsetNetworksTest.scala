package kpn.core.mongo.changes

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NetworkRepositoryImpl

class MongoQuerySubsetNetworksTest extends UnitTest with SharedTestObjects {

  test("subset networks") {
    withDatabase { database =>
      val repository = new NetworkRepositoryImpl(null, true, database)

      // sorting order different from 'by network name'
      repository.save(newNetworkInfo(newNetworkAttributes(1, Some(Country.nl), NetworkType.cycling, name = "nl-rcn-2")))
      repository.save(newNetworkInfo(newNetworkAttributes(2, Some(Country.be), NetworkType.hiking, name = "be-rwn-2")))
      repository.save(newNetworkInfo(newNetworkAttributes(3, Some(Country.be), NetworkType.hiking, name = "be-rwn-1")))
      repository.save(newNetworkInfo(newNetworkAttributes(4, Some(Country.nl), NetworkType.cycling, name = "nl-rcn-1")))

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
}
