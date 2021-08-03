package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.route.RouteMap
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.RouteMember
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.segment.FragmentMap
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute

case class RouteAnalysisContext(
  analysisContext: AnalysisContext,
  relation: Relation,
  loadedRoute: LoadedRoute,
  orphan: Boolean,
  routeNodeInfos: Map[Long, RouteNodeInfo],
  active: Boolean = true,
  proposed: Boolean = false,
  facts: Seq[Fact] = Seq.empty,
  unexpectedNodeIds: Option[Seq[Long]] = None,
  unexpectedRelationIds: Option[Seq[Long]] = None,
  routeNameAnalysis: Option[RouteNameAnalysis] = None,
  routeNodeAnalysis: Option[RouteNodeAnalysis] = None,
  expectedName: Option[String] = None,
  suspiciousWayIds: Option[Seq[Long]] = None,
  fragmentMap: Option[FragmentMap] = None,
  structure: Option[RouteStructure] = None,
  routeMembers: Option[Seq[RouteMember]] = None,
  routeMap: Option[RouteMap] = None,
  ways: Option[Seq[Way]] = None,
  allWayNodes: Option[Seq[Node]] = None,
  streets: Option[Seq[String]] = None,
  geometryDigest: Option[String] = None,
  locationAnalysis: Option[RouteLocationAnalysis] = None,
  lastSurvey: Option[Day] = None,
  labels: Seq[String] = Seq.empty,
  tiles: Seq[String] = Seq.empty
) {

  def scopedNetworkType: ScopedNetworkType = loadedRoute.scopedNetworkType

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

  def connection: Boolean = relation.tags.has("state", "connection")

}
