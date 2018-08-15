package kpn.core.engine.changes.ignore

import kpn.core.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.load.data.LoadedNetwork
import kpn.core.test.TestData
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkType
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class IgnoredNetworkAnalyzerTest extends FunSuite with Matchers with MockFactory {

  test("ignore network in foreign country") {

    pending

    val data = new TestData() {
      networkNode(1001, "01")
      networkRelation(1, "name", Seq(newMember("node", 1001)))
    }

    assertIgnoreReason(data, None, Some(Fact.IgnoreForeignCountry))
  }

  test("ignore network without any network nodes") {

    pending

    val data = new TestData() {
      networkRelation(1, "name", Seq())
    }

    assertIgnoreReason(data, Some(Fact.IgnoreNoNetworkNodes))
  }

  test("watch network with network node reference in way") {

    pending

    val data = new TestData() {
      networkNode(1001, "01")
      node(1002)
      way(101, 1001, 1002)
      route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      networkRelation(1, "name", Seq(newMember("relation", 11)))
    }

    assertIgnoreReason(data, None)
  }

  test("watch network with network node reference in route relation") {

    pending

    val data = new TestData() {
      networkNode(1001, "01")
      route(11, "01-02",
        Seq(
          newMember("node", 1001)
        )
      )
      networkRelation(1, "name", Seq(newMember("relation", 11)))
    }

    assertIgnoreReason(data, None)
  }

  test("watch network with network node reference in network relation") {

    pending

    val data = new TestData() {
      networkNode(1001, "01")
      networkRelation(1, "name", Seq(newMember("node", 1001)))
    }

    assertIgnoreReason(data, None)
  }

  private def assertIgnoreReason(testData: TestData, expected: Option[Fact]): Unit = {
    assertIgnoreReason(testData, Some(Country.nl), expected)
  }

  private def assertIgnoreReason(testData: TestData, country: Option[Country], expected: Option[Fact]): Unit = {

    val countryAnalyzer = stub[CountryAnalyzer]
    (countryAnalyzer.relationCountry _).when(*).returns(country)

    val analyzer = new IgnoredNetworkAnalyzerImpl(countryAnalyzer)

    val data = testData.data
    val loadedNetwork = LoadedNetwork(1, NetworkType.hiking, "network", data, data.relations(1))

    val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(countryAnalyzer)
    val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)

    analyzer.analyze(networkRelationAnalysis, loadedNetwork) should equal(expected)
  }
}
