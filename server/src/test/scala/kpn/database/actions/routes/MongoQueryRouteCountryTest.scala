package kpn.database.actions.routes

import kpn.api.common.Country
import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryRouteCountryTest extends UnitTest with SharedTestObjects {

  test("read route country from network collections") {
    withDatabase { database =>

      database.baseNetworks.save(
        newBaseNetworkDoc(
          _id = 1,
          routeIds = Seq(11),
        )
      )
      database.networks.save(
        newNetworkDoc(
          _id = 1,
          country = Some(Country.nl),
        )
      )
      new MongoQueryRouteCountry(database).execute(11) should equal(Some(Country.nl))
      new MongoQueryRouteCountry(database).execute(12) should equal(None)
    }
  }

  test("no networks") {
    withDatabase { database =>
      new MongoQueryRouteCountry(database).execute(11) should equal(None)
    }
  }

  test("no active base network document") {
    withDatabase { database =>

      database.baseNetworks.save(
        newBaseNetworkDoc(
          _id = 1,
          active = false,
          routeIds = Seq(11)
        )
      )
      database.networks.save(
        newNetworkDoc(
          _id = 1,
          country = Some(Country.nl),
        )
      )
      new MongoQueryRouteCountry(database).execute(11) should equal(None)
    }
  }

  test("no active network document") {
    withDatabase { database =>

      database.baseNetworks.save(
        newBaseNetworkDoc(
          _id = 1,
          routeIds = Seq(11)
        )
      )
      database.networks.save(
        newNetworkDoc(
          _id = 1,
          active = false,
          country = Some(Country.nl),
        )
      )
      new MongoQueryRouteCountry(database).execute(11) should equal(None)
    }
  }
}
