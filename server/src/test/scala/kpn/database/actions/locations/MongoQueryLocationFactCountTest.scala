package kpn.database.actions.locations

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.test.MongoTest
import kpn.server.analyzer.engine.analysis.location.LocationSubset

class MongoQueryLocationFactCountTest extends MongoTest {

  test("node fact count") {

    database.nodes.save(
      newNodeDoc(
        1001L,
        labels = Seq(
          Label.facts,
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        ),
        facts = Seq(Fact.NodeInvalidSurveyDate),
        names = Seq(
          newNodeName(name = "01")
        )
      )
    )

    database.nodes.save(
      newNodeDoc(
        1002L,
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        ),
        names = Seq(
          newNodeName(name = "02")
        )
      )
    )

    database.nodes.save(
      newNodeDoc(
        1003L,
        active = false,
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        ),
        names = Seq(
          newNodeName(name = "03")
        )
      )
    )

    val query = new MongoQueryLocationFactCount(database)
    query.execute(LocationSubset("", RouteType.hiking, Seq("be"))) should equal(1L)
    query.execute(LocationSubset("", RouteType.hiking, Seq("nl"))) should equal(0L)
    query.execute(LocationSubset("", RouteType.cycling, Seq("be"))) should equal(0L)
  }

  test("route fact count") {

    database.routes.save(
      newRouteDoc(
        newRouteSummary(101L),
        labels = Seq(
          Label.facts,
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        ),
        facts = Seq(Fact.RouteWithoutWays, Fact.RouteBroken),
      )
    )

    database.routes.save(
      newRouteDoc(
        newRouteSummary(102L),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    database.routes.save(
      newRouteDoc(
        newRouteSummary(103L),
        active = false,
        labels = Seq(
          Label.facts,
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        ),
        facts = Seq(Fact.RouteWithoutWays, Fact.RouteBroken),
      )
    )

    val query = new MongoQueryLocationFactCount(database)
    query.execute(LocationSubset("", RouteType.hiking, Seq("be"))) should equal(1L)
    query.execute(LocationSubset("", RouteType.hiking, Seq("nl"))) should equal(0L)
    query.execute(LocationSubset("", RouteType.cycling, Seq("be"))) should equal(0L)
  }
}
