package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.Bounds
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkType
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteScope
import kpn.api.common.data.Node
import kpn.api.common.route.RouteEdge
import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.RouteMemberInfo
import kpn.api.custom.ScopedNetworkType
import kpn.core.doc.RouteDetailPath
import kpn.core.doc.RouteDetailSegment
import kpn.core.doc.RouteDetailSegmentElement
import kpn.core.doc.RouteRelation
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
  _scopes: Option[Seq[RouteScope]] = None,
  scopedNetworkTypeOption: Option[ScopedNetworkType] = None,
  _countries: Option[Seq[Country]] = None,
  _links: Option[RouteLinks] = None,
  _analysisSegments: Option[Seq[RouteAnalysisSegment]] = None,
  _segments: Option[Seq[RouteDetailSegment]] = None,
  _segmentElements: Option[Seq[RouteDetailSegmentElement]] = None,
  _paths: Option[Seq[RouteDetailPath]] = None,
  _bounds: Option[Option[Bounds]] = None,
  routeNodeInfos: Map[Long, RouteNodeInfo] = Map.empty,
  _unexpectedNodeIds: Option[Seq[Long]] = None,
  _unexpectedRelationIds: Option[Seq[Long]] = None,
  _routeNameAnalysis: Option[RouteNameAnalysis] = None,
  _routeNodesAnalysis: Option[RouteNodesAnalysis] = None,
  expectedName: Option[String] = None,
  suspiciousWayIds: Option[Seq[Long]] = None,
  _oneWayRouteForward: Option[Boolean] = None,
  _oneWayRouteBackward: Option[Boolean] = None,
  _structure: Option[Structure] = None,
  _routeMembers: Option[Seq[RouteMemberInfo]] = None,
  allWayNodes: Option[Seq[Node]] = None,
  _geometryDigest: Option[String] = None,
  _locationAnalysis: Option[RouteLocationAnalysis] = None,
  lastSurvey: Option[Day] = None,
  labels: Seq[String] = Seq.empty,
  _tileAnalysis: Option[RouteTileAnalysis] = None,
  tiles: Seq[String] = Seq.empty,
  _tileDatas: Option[Seq[RouteTileData]] = None,
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

  def scopes: Seq[RouteScope] = _scopes.getOrElse(throw new PreconditionMissingException)

  def countries: Seq[Country] = _countries.getOrElse(throw new PreconditionMissingException)

  def links: RouteLinks = _links.getOrElse(throw new PreconditionMissingException)

  def analysisSegments: Seq[RouteAnalysisSegment] = _analysisSegments.getOrElse(throw new PreconditionMissingException)

  def segments: Seq[RouteDetailSegment] = _segments.getOrElse(throw new PreconditionMissingException)

  def segmentElements: Seq[RouteDetailSegmentElement] = _segmentElements.getOrElse(throw new PreconditionMissingException)

  def paths: Seq[RouteDetailPath] = _paths.getOrElse(throw new PreconditionMissingException)

  def bounds: Option[Bounds] = _bounds.getOrElse(throw new PreconditionMissingException)

  def unexpectedNodeIds: Seq[Long] = _unexpectedNodeIds.getOrElse(throw new PreconditionMissingException)

  def unexpectedRelationIds: Seq[Long] = _unexpectedRelationIds.getOrElse(throw new PreconditionMissingException)

  def routeNameAnalysis: RouteNameAnalysis = _routeNameAnalysis.getOrElse(throw new PreconditionMissingException)

  def routeNodesAnalysis: RouteNodesAnalysis = _routeNodesAnalysis.getOrElse(throw new PreconditionMissingException)

  def oneWayRouteForward: Boolean = _oneWayRouteForward.getOrElse(throw new PreconditionMissingException)

  def oneWayRouteBackward: Boolean = _oneWayRouteBackward.getOrElse(throw new PreconditionMissingException)

  def structure: Structure = _structure.getOrElse(throw new PreconditionMissingException)

  def routeMembers: Seq[RouteMemberInfo] = _routeMembers.getOrElse(throw new PreconditionMissingException)

  def geometryDigest: String = _geometryDigest.getOrElse(throw new PreconditionMissingException)

  def locationAnalysis: RouteLocationAnalysis = _locationAnalysis.getOrElse(throw new PreconditionMissingException)

  def tileAnalysis: RouteTileAnalysis = _tileAnalysis.getOrElse(throw new PreconditionMissingException)

  def tileDatas: Seq[RouteTileData] = _tileDatas.getOrElse(throw new PreconditionMissingException)
}
