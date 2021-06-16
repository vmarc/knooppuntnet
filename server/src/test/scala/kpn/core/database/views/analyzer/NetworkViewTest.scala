package kpn.core.database.views.analyzer

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.api.custom.Subset
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NetworkRepositoryImpl

class NetworkViewTest extends UnitTest with SharedTestObjects {

  test("get subset network attributes") {

    withCouchDatabase { database =>

      val repository = new NetworkRepositoryImpl(null, database, false)

      // sorting order different from 'by network name'
      repository.save(newNetworkInfo(newNetworkAttributes(1, Some(Country.nl), cycling, name = "nl-rcn-2")))
      repository.save(newNetworkInfo(newNetworkAttributes(2, Some(Country.be), hiking, name = "be-rwn-2")))
      repository.save(newNetworkInfo(newNetworkAttributes(3, Some(Country.be), hiking, name = "be-rwn-1")))
      repository.save(newNetworkInfo(newNetworkAttributes(4, Some(Country.nl), cycling, name = "nl-rcn-1")))

      NetworkView.query(database, Subset.beHiking, stale = false) should matchTo(
        Seq(
          newNetworkAttributes(3, Some(Country.be), hiking, name = "be-rwn-1"),
          newNetworkAttributes(2, Some(Country.be), hiking, name = "be-rwn-2")
        )
      )

      NetworkView.query(database, Subset.nlBicycle, stale = false) should matchTo(
        Seq(
          newNetworkAttributes(4, Some(Country.nl), cycling, name = "nl-rcn-1"),
          newNetworkAttributes(1, Some(Country.nl), cycling, name = "nl-rcn-2")
        )
      )
    }
  }

  test("non-active networks are not included") {

    withCouchDatabase { database =>

      val repository = new NetworkRepositoryImpl(null, database, false)

      // sorting order different from 'by network name'
      repository.save(newNetworkInfo(newNetworkAttributes(1, Some(Country.nl), cycling, name = "nl-rcn-2")))
      repository.save(newNetworkInfo(newNetworkAttributes(2, Some(Country.be), hiking, name = "be-rwn-2"), active = false))

      NetworkView.query(database, Subset.nlBicycle, stale = false) should matchTo(
        Seq(
          newNetworkAttributes(1, Some(Country.nl), cycling, name = "nl-rcn-2")
        )
      )
    }
  }
}
