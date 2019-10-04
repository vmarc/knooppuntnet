package kpn.core.engine.analysis.route.domain

import kpn.core.analysis.NetworkNode
import kpn.core.analysis.RouteMember
import kpn.core.engine.analysis.Interpreter
import kpn.core.engine.analysis.route.RouteNameAnalysis
import kpn.core.engine.analysis.route.RouteNodeAnalysis
import kpn.core.engine.analysis.route.RouteStructure
import kpn.core.engine.analysis.route.analyzers.Overlap
import kpn.core.engine.analysis.route.segment.Fragment
import kpn.core.load.data.LoadedRoute
import kpn.shared.Fact
import kpn.shared.data.Node
import kpn.shared.data.Way
import kpn.shared.route.RouteMap

case class RouteAnalysisContext(
  networkNodes: Map[Long, NetworkNode],
  loadedRoute: LoadedRoute,
  orphan: Boolean,
  interpreter: Interpreter,
  facts: Seq[Fact] = Seq.empty,
  unexpectedNodeIds: Option[Seq[Long]] = None,
  unexpectedRelationIds: Option[Seq[Long]] = None,
  routeNameAnalysis: Option[RouteNameAnalysis] = None,
  routeNodeAnalysis: Option[RouteNodeAnalysis] = None,
  expectedName: Option[String] = None,
  suspiciousWayIds: Option[Seq[Long]] = None,
  overlappingWays: Option[Seq[Overlap]] = None,
  fragments: Option[Seq[Fragment]] = None,
  structure: Option[RouteStructure] = None,
  routeMembers: Option[Seq[RouteMember]] = None,
  routeMap: Option[RouteMap] = None,
  ways: Option[Seq[Way]] = None,
  allWayNodes: Option[Seq[Node]] = None,
  streets: Option[Seq[String]] = None

) {

  def withFact(fact: Fact): RouteAnalysisContext = {
    copy(facts = facts :+ fact)
  }

  def replaceAllFactsWith(fact: Fact): RouteAnalysisContext = {
    copy(facts = Seq(fact))
  }

  def withFact(condition: Boolean, fact: Fact): RouteAnalysisContext = {
    if (condition) {
      withFact(fact)
    }
    else {
      this
    }
  }

  def withFacts(newFacts: Fact*): RouteAnalysisContext = {
    if (newFacts.nonEmpty) {
      copy(facts = facts ++ newFacts)
    }
    else {
      this
    }
  }

  def withoutFacts(excludedFacts: Fact*): RouteAnalysisContext = {
    if (excludedFacts.nonEmpty) {
      copy(facts = facts.filterNot(excludedFacts.contains))
    }
    else {
      this
    }
  }

  def hasFact(expectedFacts: Fact*): Boolean = {
    expectedFacts.exists(f => facts.contains(f))
  }

  def connection: Boolean = loadedRoute.relation.tags.has("state", "connection")

}
