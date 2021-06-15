package kpn.core.mongo.actions.subsets

import kpn.api.common.OrphanRouteInfo
import kpn.api.common.SharedTestObjects
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.OrphanRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class MongoQuerySubsetOrphanRoutesTest extends UnitTest with SharedTestObjects {

  test("active orphan route") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(null, true, database)
      val orphanRepository = new OrphanRepositoryImpl(null, true, database)

      routeRepository.save(createRoute())

      orphanRepository.orphanRoutes(Subset.nlHiking) should matchTo(
        Seq(
          OrphanRouteInfo(
            id = 100L,
            name = "01-02",
            meters = 123L,
            isBroken = false,
            accessible = true,
            lastSurvey = "2020-08-11",
            lastUpdated = Timestamp(2020, 8, 11)
          )
        )
      )
    }
  }

  test("do not include routes that are not active") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(null, true, database)
      val orphanRepository = new OrphanRepositoryImpl(null, true, database)

      routeRepository.save(createRoute().copy(active = false))

      orphanRepository.orphanRoutes(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("do not include routes that are not orphan") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(null, true, database)
      val orphanRepository = new OrphanRepositoryImpl(null, true, database)

      routeRepository.save(createRoute().copy(orphan = false))

      orphanRepository.orphanRoutes(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("do not include routes in another country") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(null, true, database)
      val orphanRepository = new OrphanRepositoryImpl(null, true, database)

      val route = createRoute()
      val modifiedRoute = route.copy(summary = route.summary.copy(country = Some(Country.be)))
      routeRepository.save(modifiedRoute)

      orphanRepository.orphanRoutes(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("do not include routes with a different networkType") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(null, true, database)
      val orphanRepository = new OrphanRepositoryImpl(null, true, database)

      val route = createRoute()
      val modifiedRoute = route.copy(summary = route.summary.copy(networkType = NetworkType.cycling))
      routeRepository.save(modifiedRoute)

      orphanRepository.orphanRoutes(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("route that is broken") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(null, true, database)
      val orphanRepository = new OrphanRepositoryImpl(null, true, database)

      val route = createRoute()
      val modifiedRoute = route.copy(summary = route.summary.copy(isBroken = true))
      routeRepository.save(modifiedRoute)

      orphanRepository.orphanRoutes(Subset.nlHiking) should matchTo(
        Seq(
          OrphanRouteInfo(
            id = 100L,
            name = "01-02",
            meters = 123L,
            isBroken = true,
            accessible = true,
            lastSurvey = "2020-08-11",
            lastUpdated = Timestamp(2020, 8, 11)
          )
        )
      )
    }
  }

  test("route that is not accessible") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(null, true, database)
      val orphanRepository = new OrphanRepositoryImpl(null, true, database)

      val route = createRoute()
      val modifiedRoute = route.copy(facts = Seq(Fact.RouteUnaccessible))
      routeRepository.save(modifiedRoute)

      orphanRepository.orphanRoutes(Subset.nlHiking) should matchTo(
        Seq(
          OrphanRouteInfo(
            id = 100L,
            name = "01-02",
            meters = 123L,
            isBroken = false,
            accessible = false,
            lastSurvey = "2020-08-11",
            lastUpdated = Timestamp(2020, 8, 11)
          )
        )
      )
    }
  }

  test("route without survey") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(null, true, database)
      val orphanRepository = new OrphanRepositoryImpl(null, true, database)

      val route = createRoute()
      val modifiedRoute = route.copy(lastSurvey = None)
      routeRepository.save(modifiedRoute)

      orphanRepository.orphanRoutes(Subset.nlHiking) should matchTo(
        Seq(
          OrphanRouteInfo(
            id = 100L,
            name = "01-02",
            meters = 123L,
            isBroken = false,
            accessible = true,
            lastSurvey = "-",
            lastUpdated = Timestamp(2020, 8, 11)
          )
        )
      )
    }
  }


  private def createRoute(): RouteInfo = {
    newRoute(
      id = 100L,
      orphan = true,
      country = Some(Country.nl),
      networkType = NetworkType.hiking,
      name = "01-02",
      meters = 123,
      lastUpdated = Timestamp(2020, 8, 11),
      lastSurvey = Some(Day(2020, 8, Some(11)))
    )
  }
}
