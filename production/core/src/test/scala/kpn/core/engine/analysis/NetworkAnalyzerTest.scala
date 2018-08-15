package kpn.core.engine.analysis

import kpn.core.analysis.Network
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.test.TestData
import kpn.shared.NetworkExtraMemberNode
import kpn.shared.NetworkExtraMemberRelation
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.data.Tags
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkAnalyzerTest extends FunSuite with Matchers with MockFactory {

  test("relation without members") {
    val d = new TestData() {
      relation(1)
    }
    val network = analyze(d)

    network.facts.networkExtraMemberNode should equal(None)
    network.facts.networkExtraMemberWay should equal(None)
    network.facts.networkExtraMemberRelation should equal(None)
  }

  test("networkExtraMemberNode") {
    val d = new TestData() {
      networkNode(1001, "01")
      node(1002)
      relation(1, Seq(newMember("node", 1001), newMember("node", 1002)))
    }
    val network = analyze(d)
    network.facts.networkExtraMemberNode should equal(Some(Seq(NetworkExtraMemberNode(1002))))
  }

  test("networkExtraMemberWay relation without members") {

    val d = new TestData() {
      way(1)
      relation(1, Seq(newMember("way", 1)))
    }

    val network = analyze(d)
    network.facts.networkExtraMemberWay should equal(Some(Seq(NetworkExtraMemberWay(1))))
  }

  test("networkExtraMemberRelation") {

    val d = new TestData() {
      relation(10, Seq(), Tags.from("network" -> "rwn", "type" -> "route"))
      relation(20, Seq(), Tags.empty)
      relation(1, Seq(newMember("relation", 10), newMember("relation", 20)))
    }

    val network = analyze(d)
    network.facts.networkExtraMemberRelation should equal(Some(Seq(NetworkExtraMemberRelation(20))))
  }

  test("routes") {

    val d = new TestData() {
      relation(10, Seq(), Tags.from("network" -> "rwn", "type" -> "route", "note" -> "01-03"))
      relation(20, Seq(), Tags.from("network" -> "rwn", "type" -> "route", "note" -> "01-02"))
      relation(30, Seq(), Tags.from("network" -> "rwn", "type" -> "route", "note" -> "02-03"))
      relation(1, Seq(
        newMember("relation", 10, "forward"),
        newMember("relation", 20, "backward"),
        newMember("relation", 30)
      )
      )
    }

    val routes = analyze(d).routes
    routes(0).routeAnalysis.route.summary.name should equal("01-02")
    routes(1).routeAnalysis.route.summary.name should equal("01-03")
    routes(0).role should equal(Some("backward"))
    routes(1).role should equal(Some("forward"))
  }

  private def analyze(d: TestData): Network = {
    val data = d.data
    val networkRelation = data.relations(1)
    val countryAnalyzer = stub[CountryAnalyzer]
    (countryAnalyzer.country _).when(*).returns(None)
    (countryAnalyzer.relationCountry _).when(*).returns(None)
    val routeAnalyzer = new MasterRouteAnalyzerImpl()
    val networkRelationAnalysis = new NetworkRelationAnalyzerImpl(countryAnalyzer).analyze(networkRelation)
    new NetworkAnalyzerImpl(countryAnalyzer, routeAnalyzer).analyze(networkRelationAnalysis, data, 1)
  }
}
