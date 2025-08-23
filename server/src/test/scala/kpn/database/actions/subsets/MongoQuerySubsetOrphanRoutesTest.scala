package kpn.database.actions.subsets

import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.custom.Day
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.doc.RouteDoc
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newOrphanRouteInfo
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary

class MongoQuerySubsetOrphanRoutesTest extends MongoTest {

  test("orphan route") {
    database.routes.save(createRouteDoc())
    assertEqual(
      new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking),
      Seq(
        newOrphanRouteInfo(
          id = 100L,
          name = "01-02",
          meters = 123,
          lastUpdated = Timestamp(2020, 8, 11),
        )
      )
    )
  }

  test("do not include routes in another country") {
    database.routes.save(createRouteDoc(country = Country.be))
    new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking) should equal(Seq.empty)
  }

  test("do not include routes with a different routeType") {
    database.routes.save(createRouteDoc(routeType = RouteType.cycling))
    new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking) should equal(Seq.empty)
  }

  test("route that is broken") {
    database.routes.save(createRouteDoc(broken = true))
    assertEqual(
      new MongoQuerySubsetOrphanRoutes(database).execute(Subset.nlHiking),
      Seq(
        newOrphanRouteInfo(
          id = 100L,
          name = "01-02",
          meters = 123,
          isBroken = true,
          lastUpdated = Timestamp(2020, 8, 11),
        )
      )
    )
  }

  private def createRouteDoc(
    country: Country = Country.nl,
    routeType: RouteType = RouteType.hiking,
    lastSurvey: Option[Day] = None,
    broken: Boolean = false
  ): RouteDoc = {
    newRouteDoc(
      newRouteSummary(
        id = 100,
        name = "01-02",
        meters = 123,
        broken = broken
      ),
      labels = Seq(
        Label.country(country),
        Label.routeType(routeType)
      ),
      lastUpdated = Timestamp(2020, 8, 11),
    )
  }
}
