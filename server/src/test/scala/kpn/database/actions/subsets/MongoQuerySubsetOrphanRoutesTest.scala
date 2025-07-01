package kpn.database.actions.subsets

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.custom.Day
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.doc.OrphanRouteDoc
import kpn.core.test.MongoTest

class MongoQuerySubsetOrphanRoutesTest extends MongoTest {

  test("orphan route") {
    database.orphanRoutes.save(createOrphanRouteDoc())
    assertEqual(
      new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking),
      Seq(
        OrphanRouteDoc(
          _id = 100L,
          Country.nl,
          Seq(RouteType.hiking),
          name = "01-02",
          meters = 123,
          facts = Seq.empty,
          lastSurvey = None,
          lastUpdated = Timestamp(2020, 8, 11),
        )
      )
    )
  }

  test("do not include routes in another country") {
    database.orphanRoutes.save(createOrphanRouteDoc(country = Country.be))
    new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking) should equal(Seq.empty)
  }

  test("do not include routes with a different routeType") {
    database.orphanRoutes.save(createOrphanRouteDoc(routeType = RouteType.cycling))
    new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking) should equal(Seq.empty)
  }

  test("route that is broken") {
    database.orphanRoutes.save(createOrphanRouteDoc(facts = Seq(Fact.RouteBroken)))
    assertEqual(
      new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking),
      Seq(
        OrphanRouteDoc(
          _id = 100L,
          Country.nl,
          Seq(RouteType.hiking),
          name = "01-02",
          meters = 123,
          facts = Seq(Fact.RouteBroken),
          lastSurvey = None,
          lastUpdated = Timestamp(2020, 8, 11),
        )
      )
    )
  }

  private def createOrphanRouteDoc(
    country: Country = Country.nl,
    routeType: RouteType = RouteType.hiking,
    lastSurvey: Option[Day] = None,
    facts: Seq[Fact] = Seq.empty
  ): OrphanRouteDoc = {
    newOrphanRouteDoc(
      _id = 100L,
      country,
      routeType,
      name = "01-02",
      meters = 123,
      facts = facts,
      lastSurvey = lastSurvey,
      lastUpdated = Timestamp(2020, 8, 11),
    )
  }
}
