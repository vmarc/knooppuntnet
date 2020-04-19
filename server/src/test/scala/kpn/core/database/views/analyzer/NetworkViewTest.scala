package kpn.core.database.views.analyzer

import kpn.api.custom.Country
import kpn.api.custom.Subset
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.NetworkRepositoryImpl
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.api.common.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("get subset network attributes") {

    withDatabase { database =>

      val repository = new NetworkRepositoryImpl(database)

      // sorting order different from 'by network name'
      repository.save(newNetworkInfo(newNetworkAttributes(1, Some(Country.nl), cycling, "nl-rcn-2")))
      repository.save(newNetworkInfo(newNetworkAttributes(2, Some(Country.be), hiking, "be-rwn-2")))
      repository.save(newNetworkInfo(newNetworkAttributes(3, Some(Country.be), hiking, "be-rwn-1")))
      repository.save(newNetworkInfo(newNetworkAttributes(4, Some(Country.nl), cycling, "nl-rcn-1")))

      NetworkView.query(database, Subset.beHiking, stale = false) should equal(
        Seq(
          newNetworkAttributes(3, Some(Country.be), hiking, "be-rwn-1"),
          newNetworkAttributes(2, Some(Country.be), hiking, "be-rwn-2")
        )
      )

      NetworkView.query(database, Subset.nlBicycle, stale = false) should equal(
        Seq(
          newNetworkAttributes(4, Some(Country.nl), cycling, "nl-rcn-1"),
          newNetworkAttributes(1, Some(Country.nl), cycling, "nl-rcn-2")
        )
      )
    }
  }

  test("non-active networks are not included") {

    withDatabase { database =>

      val repository = new NetworkRepositoryImpl(database)

      // sorting order different from 'by network name'
      repository.save(newNetworkInfo(newNetworkAttributes(1, Some(Country.nl), cycling, "nl-rcn-2")))
      repository.save(newNetworkInfo(newNetworkAttributes(2, Some(Country.be), hiking, "be-rwn-2"), active = false))

      NetworkView.query(database, Subset.nlBicycle, stale = false) should equal(
        Seq(
          newNetworkAttributes(1, Some(Country.nl), cycling, "nl-rcn-2")
        )
      )
    }
  }
}
