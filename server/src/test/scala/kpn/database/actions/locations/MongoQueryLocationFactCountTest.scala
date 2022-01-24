package kpn.database.actions.locations

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.core.doc.Label
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryLocationFactCountTest extends UnitTest with SharedTestObjects {

  test("node fact count") {

    withDatabase() { database =>

      database.nodes.save(
        newNodeDoc(
          1001L,
          labels = Seq(
            Label.active,
            Label.facts,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
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
            Label.active,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "02")
          )
        )
      )

      database.nodes.save(
        newNodeDoc(
          1003L,
          labels = Seq( // not active
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "03")
          )
        )
      )

      val query = new MongoQueryLocationFactCount(database)
      query.execute(NetworkType.hiking, "be") should equal(1L)
      query.execute(NetworkType.hiking, "nl") should equal(0L)
      query.execute(NetworkType.cycling, "be") should equal(0L)
    }
  }

  test("route fact count") {

    withDatabase(keepDatabaseAfterTest = true) { database =>

      database.routes.save(
        newRouteDoc(
          newRouteSummary(101L),
          labels = Seq(
            Label.active,
            Label.facts,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          facts = Seq(Fact.RouteWithoutWays, Fact.RouteBroken),
        )
      )

      database.routes.save(
        newRouteDoc(
          newRouteSummary(102L),
          labels = Seq(
            Label.active,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          )
        )
      )

      database.routes.save(
        newRouteDoc(
          newRouteSummary(103L),
          labels = Seq(
            Label.facts,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          active = false,
          facts = Seq(Fact.RouteWithoutWays, Fact.RouteBroken),
        )
      )

      val query = new MongoQueryLocationFactCount(database)
      query.execute(NetworkType.hiking, "be") should equal(1L)
      query.execute(NetworkType.hiking, "nl") should equal(0L)
      query.execute(NetworkType.cycling, "be") should equal(0L)
    }
  }
}
