package kpn.database.actions.routes

import kpn.api.common.Country
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.core.test.MongoTest

class MongoQueryRouteCountryTest extends MongoTest {

  test("read route country from network collections") {

    database.baseNetworks.save(
      newBaseNetworkDoc(
        _id = 1,
        members = Seq(
          RawMember(MemberType.Relation, 11, None)
        )
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

  test("no networks") {
    new MongoQueryRouteCountry(database).execute(11) should equal(None)
  }

  test("no active base network document") {

    database.baseNetworks.save(
      newBaseNetworkDoc(
        _id = 1,
        active = false,
        members = Seq(
          RawMember(MemberType.Relation, 11, None)
        )
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

  test("no active network document") {

    database.baseNetworks.save(
      newBaseNetworkDoc(
        _id = 1,
        members = Seq(
          RawMember(MemberType.Relation, 11, None)
        )
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
