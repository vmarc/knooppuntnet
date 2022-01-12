package kpn.database.actions.subsets

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.doc.OrphanRouteDoc
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQuerySubsetOrphanRoutesTest extends UnitTest with SharedTestObjects {

  test("orphan route") {
    withDatabase { database =>

      database.orphanRoutes.save(createOrphanRouteDoc())

      new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking).shouldMatchTo(
        Seq(
          OrphanRouteDoc(
            _id = 100L,
            Country.nl,
            NetworkType.hiking,
            name = "01-02",
            meters = 123,
            facts = Seq.empty,
            lastSurvey = None,
            lastUpdated = Timestamp(2020, 8, 11),
          )
        )
      )
    }
  }

  test("do not include routes in another country") {
    withDatabase { database =>
      database.orphanRoutes.save(createOrphanRouteDoc(country = Country.be))
      new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("do not include routes with a different networkType") {
    withDatabase { database =>
      database.orphanRoutes.save(createOrphanRouteDoc(networkType = NetworkType.cycling))
      new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("route that is broken") {
    withDatabase { database =>
      database.orphanRoutes.save(createOrphanRouteDoc(facts = Seq(Fact.RouteBroken)))
      new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking).shouldMatchTo(
        Seq(
          OrphanRouteDoc(
            _id = 100L,
            Country.nl,
            NetworkType.hiking,
            name = "01-02",
            meters = 123,
            facts = Seq(Fact.RouteBroken),
            lastSurvey = None,
            lastUpdated = Timestamp(2020, 8, 11),
          )
        )
      )
    }
  }

  private def createOrphanRouteDoc(
    country: Country = Country.nl,
    networkType: NetworkType = NetworkType.hiking,
    lastSurvey: Option[Day] = None,
    facts: Seq[Fact] = Seq.empty
  ): OrphanRouteDoc = {
    newOrphanRouteDoc(
      _id = 100L,
      country,
      networkType,
      name = "01-02",
      meters = 123,
      facts = facts,
      lastSurvey = lastSurvey,
      lastUpdated = Timestamp(2020, 8, 11),
    )
  }
}
