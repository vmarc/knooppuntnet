package kpn.core.engine.changes.ignore

import kpn.core.load.data.LoadedNode
import kpn.core.test.TestData
import kpn.shared.Country
import kpn.shared.Fact
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class IgnoredNodeAnalyzerTest extends FunSuite with Matchers with MockFactory {

  test("node not ignored") {
    new IgnoredNodeAnalyzerImpl().analyze(node(Some(Country.nl))) should equal(Seq())
  }

  test("node ignored because in foreign country") {
    new IgnoredNodeAnalyzerImpl().analyze(node(None)) should equal(Seq(Fact.IgnoreForeignCountry))
  }

  private def node(country: Option[Country]): LoadedNode = {
    val node = new TestData() { node(1001) }.data.nodes(1001)
    LoadedNode(country, Seq(), "", node)
  }
}
