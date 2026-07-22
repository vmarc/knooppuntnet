package kpn.database.actions.locations

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNodeBaseData
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newNodeName
import kpn.core.test.TestObjects.newRouteDoc
import kpn.server.analyzer.engine.analysis.location.LocationSubset

class MongoQueryLocationFactCountTest extends MongoTest {

  test("node fact count") {

    database.nodes.save(
      newNodeDoc(
        1001L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "01")
          )
        ),
        labels = Seq(
          Label.facts,
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        ),
        facts = Seq(Fact.NodeInvalidSurveyDate)
      )
    )

    database.nodes.save(
      newNodeDoc(
        1002L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "02")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    database.nodes.save(
      newNodeDoc(
        1003L,
        active = false,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "03")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
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
        101L,
        labels = Seq(
          Label.facts,
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        ),
        facts = Seq(Fact.RouteWithoutWays),
      )
    )

    database.routes.save(
      newRouteDoc(
        102L,
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    database.routes.save(
      newRouteDoc(
        103L,
        active = false,
        labels = Seq(
          Label.facts,
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        ),
        facts = Seq(Fact.RouteWithoutWays),
      )
    )

    val query = new MongoQueryLocationFactCount(database)
    query.execute(LocationSubset("", RouteType.hiking, Seq("be"))) should equal(1L)
    query.execute(LocationSubset("", RouteType.hiking, Seq("nl"))) should equal(0L)
    query.execute(LocationSubset("", RouteType.cycling, Seq("be"))) should equal(0L)
  }
}
