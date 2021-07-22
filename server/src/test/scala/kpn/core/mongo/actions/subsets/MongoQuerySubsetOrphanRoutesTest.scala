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
      val routeRepository = new RouteRepositoryImpl(database, null, true)
      val orphanRepository = new OrphanRepositoryImpl(database, null, true)

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
      val routeRepository = new RouteRepositoryImpl(database, null, true)
      val orphanRepository = new OrphanRepositoryImpl(database, null, true)

      routeRepository.save(createRoute(active = false))

      orphanRepository.orphanRoutes(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("do not include routes that are not orphan") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database, null, true)
      val orphanRepository = new OrphanRepositoryImpl(database, null, true)

      routeRepository.save(createRoute(orphan = false))

      orphanRepository.orphanRoutes(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("do not include routes in another country") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database, null, true)
      val orphanRepository = new OrphanRepositoryImpl(database, null, true)

      val route = createRoute(country = Country.be)
      routeRepository.save(route)

      orphanRepository.orphanRoutes(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("do not include routes with a different networkType") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database, null, true)
      val orphanRepository = new OrphanRepositoryImpl(database, null, true)

      val route = createRoute(networkType = NetworkType.cycling)
      routeRepository.save(route)

      orphanRepository.orphanRoutes(Subset.nlHiking) should equal(Seq.empty)
    }
  }

  test("route that is broken") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database, null, true)
      val orphanRepository = new OrphanRepositoryImpl(database, null, true)

      val route = createRoute(facts = Seq(Fact.RouteBroken))
      routeRepository.save(route)

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
      val routeRepository = new RouteRepositoryImpl(database, null, true)
      val orphanRepository = new OrphanRepositoryImpl(database, null, true)

      val route = createRoute(facts = Seq(Fact.RouteUnaccessible))
      routeRepository.save(route)

      orphanRepository.orphanRoutes(Subset.nlHiking) should matchTo(
        Seq(
          OrphanRouteInfo(
            id = 100L,
            name = "01-02",
            meters = 123L,
            isBroken = true,
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
      val routeRepository = new RouteRepositoryImpl(database, null, true)
      val orphanRepository = new OrphanRepositoryImpl(database, null, true)

      val route = createRoute(lastSurvey = None)
      routeRepository.save(route)

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

  private def createRoute(
    active: Boolean = true,
    orphan: Boolean = true,
    country: Country = Country.nl,
    networkType: NetworkType = NetworkType.hiking,
    lastSurvey: Option[Day] = Some(Day(2020, 8, Some(11))),
    facts: Seq[Fact] = Seq.empty
  ): RouteInfo = {
    newRoute(
      id = 100L,
      labels = Seq(
        if (active) Some("active") else None,
        if (orphan) Some("orphan") else None,
        if (lastSurvey.isDefined) Some("survey") else None,
        if (facts.nonEmpty) Some("facts") else None,
        Some("location-" + country.domain),
        Some("network-type-" + networkType.name),
      ).flatten,
      orphan = orphan,
      country = Some(country),
      networkType = networkType,
      name = "01-02",
      meters = 123,
      facts = facts,
      lastUpdated = Timestamp(2020, 8, 11),
      lastSurvey = lastSurvey
    )
  }
}
