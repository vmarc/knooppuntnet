package kpn.core.engine.analysis.caseStudies

import kpn.core.changes.RelationAnalyzer
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.load.data.LoadedRoute
import kpn.core.loadOld.Parser
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkType
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.xml.InputSource
import scala.xml.XML

class Issue48_RouteWithSingleNodeWayTest extends FunSuite with Matchers {

  test("ingore ways with less than 2 nodes in route analysis") {
    val loadedRoute = readRoute()
    val routeAnalyzer = new MasterRouteAnalyzerImpl(new AccessibilityAnalyzerImpl())
    val routeAnalysis = routeAnalyzer.analyze(Map(), loadedRoute, orphan = false)
    routeAnalysis.route.facts.contains(Fact.RouteSuspiciousWays) should equal(true)
  }

  private def readRoute(): LoadedRoute = {
    val data = readData()
    val routeRelation = data.relations(2941800L)
    val name = RelationAnalyzer.routeName(routeRelation)
    LoadedRoute(Some(Country.nl), NetworkType.hiking, name, data, routeRelation)
  }

  private def readData(): Data = {
    val stream = getClass.getResourceAsStream("/case-studies/network-2243640.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser().parse(xml)
    new DataBuilder(NetworkType.hiking, rawData).data
  }

}
