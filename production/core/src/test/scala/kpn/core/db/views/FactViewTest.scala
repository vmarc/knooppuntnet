package kpn.core.db.views

import kpn.core.db.TestDocBuilder
import kpn.core.db.couch.Couch
import kpn.core.db.views.FactView.FactViewKey
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Fact
import kpn.shared.Subset
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FactViewTest extends FunSuite with Matchers {

  test("rows") {

    withDatabase { database =>

      val networkId = 5L

      new TestDocBuilder(database) {
        val detail = Some(
          networkInfoDetail(
            nodes = Seq(
              newNetworkNodeInfo2(
                1001L,
                "01",
                facts = Seq(
                  Fact.NodeMemberMissing
                )
              )
            ),
            routes = Seq(
              networkRouteInfo(
                10L,
                facts = Seq(
                  Fact.RouteBroken,
                  Fact.RouteNameMissing
                )
              )
            )
          )
        )
        network(
          networkId,
          Subset.nlHiking,
          "network-name",
          facts = Seq(
            Fact.NameMissing,
            Fact.IgnoreForeignCountry
          ),
          detail = detail
        )
      }

      val rows = database.query(AnalyzerDesign, FactView, Couch.uiTimeout, stale = false)().map(FactView.convert)

      rows should equal(
        Seq(
          FactViewKey("nl", "rwn", "IgnoreForeignCountry", "network-name", networkId),
          FactViewKey("nl", "rwn", "NameMissing", "network-name", networkId),
          FactViewKey("nl", "rwn", "NodeMemberMissing", "network-name", networkId),
          FactViewKey("nl", "rwn", "RouteBroken", "network-name", networkId),
          FactViewKey("nl", "rwn", "RouteNameMissing", "network-name", networkId)
        )
      )
    }
  }
}
