package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.data.Node
import kpn.api.common.route.RouteEdge
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.RouteMember
import kpn.core.tools.next.domain.RouteRelation
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.engine.context.PreconditionMissingException
import kpn.server.analyzer.engine.tiles.domain.RouteTileAnalysis

case class RouteDetailAnalysisContext(
  relation: Relation,
  hierarchy: Option[RouteRelation],
  // analysis results start here...
  active: Boolean = true,
  superRoute: Boolean = false,
  nodeNetwork: Boolean = false,
  proposed: Boolean = false,
  _networkTypes: Option[Seq[NetworkType]] = None,
  scopedNetworkTypeOption: Option[ScopedNetworkType] = None,
  _countries: Option[Seq[Country]] = None,
  _links: Option[RouteLinks] = None,
  _segments: Option[Seq[RouteAnalysisSegment]] = None,
  routeNodeInfos: Map[Long, RouteNodeInfo] = Map.empty,
  _unexpectedNodeIds: Option[Seq[Long]] = None,
  _unexpectedRelationIds: Option[Seq[Long]] = None,
  _routeNameAnalysis: Option[RouteNameAnalysis] = None,
  _nodes: Option[RouteAnalysisNodes] = None,
  expectedName: Option[String] = None,
  suspiciousWayIds: Option[Seq[Long]] = None,
  _structure: Option[Structure] = None,
  _routeMembers: Option[Seq[RouteMember]] = None,
  allWayNodes: Option[Seq[Node]] = None,
  _geometryDigest: Option[String] = None,
  _locationAnalysis: Option[RouteLocationAnalysis] = None,
  lastSurvey: Option[Day] = None,
  labels: Seq[String] = Seq.empty,
  _tileAnalysis: Option[RouteTileAnalysis] = None,
  tiles: Seq[String] = Seq.empty,
  elementIds: ElementIds = ElementIds(),
  edges: Seq[RouteEdge] = Seq.empty,
  facts: Seq[Fact] = Seq.empty,
  traceEnabled: Boolean = false,
  abort: Boolean = false
) {

  def scopedNetworkType: ScopedNetworkType = {
    scopedNetworkTypeOption.getOrElse {
      throw new IllegalArgumentException("trying to use scopedNetworkType before definition")
    }
  }

  def withFact(fact: Fact): RouteDetailAnalysisContext = {
    copy(facts = facts :+ fact)
  }

  def withFact(condition: Boolean, fact: Fact): RouteDetailAnalysisContext = {
    if (condition) {
      withFact(fact)
    }
    else {
      this
    }
  }

  def withFacts(newFacts: Fact*): RouteDetailAnalysisContext = {
    if (newFacts.nonEmpty) {
      copy(facts = facts ++ newFacts)
    }
    else {
      this
    }
  }

  def withoutFacts(excludedFacts: Fact*): RouteDetailAnalysisContext = {
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

  def connection: Boolean = relation.hasTag("state", "connection")

  // prerequisite checking accessors

  def networkTypes: Seq[NetworkType] = _networkTypes.getOrElse(throw new PreconditionMissingException)

  def countries: Seq[Country] = _countries.getOrElse(throw new PreconditionMissingException)

  def links: RouteLinks = _links.getOrElse(throw new PreconditionMissingException)

  def segments: Seq[RouteAnalysisSegment] = _segments.getOrElse(throw new PreconditionMissingException)

  def unexpectedNodeIds: Seq[Long] = _unexpectedNodeIds.getOrElse(throw new PreconditionMissingException)

  def unexpectedRelationIds: Seq[Long] = _unexpectedRelationIds.getOrElse(throw new PreconditionMissingException)

  def routeNameAnalysis: RouteNameAnalysis = _routeNameAnalysis.getOrElse(throw new PreconditionMissingException)

  def nodes: RouteAnalysisNodes = _nodes.getOrElse(throw new PreconditionMissingException)

  def structure: Structure = _structure.getOrElse(throw new PreconditionMissingException)

  def routeMembers: Seq[RouteMember] = _routeMembers.getOrElse(throw new PreconditionMissingException)

  def geometryDigest: String = _geometryDigest.getOrElse(throw new PreconditionMissingException)

  def locationAnalysis: RouteLocationAnalysis = _locationAnalysis.getOrElse(throw new PreconditionMissingException)

  def tileAnalysis: RouteTileAnalysis = _tileAnalysis.getOrElse(throw new PreconditionMissingException)
}
