package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.diff.RouteData
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteMap
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.RouteMember
import kpn.core.tools.next.domain.RouteRelation
import kpn.server.analyzer.engine.analysis.route.OldRouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalysisBuilder
import kpn.server.analyzer.engine.analysis.route.segment.FragmentMap
import kpn.server.analyzer.engine.analysis.route.structure.RouteAnalysisNodes
import kpn.server.analyzer.engine.analysis.route.structure.RouteAnalysisSegment
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinks
import kpn.server.analyzer.engine.analysis.route.structure.Structure
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
  country: Option[Country] = None,
  _links: Option[RouteLinks] = None,
  _segments: Option[Seq[RouteAnalysisSegment]] = None,
  routeNodeInfos: Map[Long, RouteNodeInfo] = Map.empty,
  facts: Seq[Fact] = Seq.empty,
  oldFacts: Seq[Fact] = Seq.empty,
  _unexpectedNodeIds: Option[Seq[Long]] = None,
  _unexpectedRelationIds: Option[Seq[Long]] = None,
  _routeNameAnalysis: Option[RouteNameAnalysis] = None,
  _oldRouteNodeAnalysis: Option[OldRouteNodeAnalysis] = None,
  _nodes: Option[RouteAnalysisNodes] = None,
  expectedName: Option[String] = None,
  suspiciousWayIds: Option[Seq[Long]] = None,
  _fragmentMap: Option[FragmentMap] = None,
  _oldStructure: Option[RouteStructure] = None,
  _structure: Option[Structure] = None,
  _routeMembers: Option[Seq[RouteMember]] = None,
  _routeMap: Option[RouteMap] = None,
  _ways: Option[Seq[Way]] = None,
  allWayNodes: Option[Seq[Node]] = None,
  _geometryDigest: Option[String] = None,
  _locationAnalysis: Option[RouteLocationAnalysis] = None,
  lastSurvey: Option[Day] = None,
  labels: Seq[String] = Seq.empty,
  _tileAnalysis: Option[RouteTileAnalysis] = None,
  tiles: Seq[String] = Seq.empty,
  elementIds: ElementIds = ElementIds(),
  edges: Seq[RouteEdge] = Seq.empty,
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

  def withOldFact(fact: Fact): RouteDetailAnalysisContext = {
    copy(oldFacts = oldFacts :+ fact)
  }

  def replaceAllFactsWith(fact: Fact): RouteDetailAnalysisContext = {
    copy(
      facts = Seq(fact),
      oldFacts = Seq(fact)
    )
  }

  def withFact(condition: Boolean, fact: Fact): RouteDetailAnalysisContext = {
    if (condition) {
      withFact(fact)
    }
    else {
      this
    }
  }

  def withOldFact(condition: Boolean, fact: Fact): RouteDetailAnalysisContext = {
    if (condition) {
      withOldFact(fact)
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

  def withOldFacts(newFacts: Fact*): RouteDetailAnalysisContext = {
    if (newFacts.nonEmpty) {
      copy(oldFacts = oldFacts ++ newFacts)
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

  def withoutOldFacts(excludedFacts: Fact*): RouteDetailAnalysisContext = {
    if (excludedFacts.nonEmpty) {
      copy(oldFacts = oldFacts.filterNot(excludedFacts.contains))
    }
    else {
      this
    }
  }

  def hasFact(expectedFacts: Fact*): Boolean = {
    expectedFacts.exists(f => facts.contains(f))
  }

  def hasOldFact(expectedFacts: Fact*): Boolean = {
    expectedFacts.exists(f => oldFacts.contains(f))
  }

  def connection: Boolean = relation.hasTag("state", "connection")

  // prerequisite checking accessors

  def networkTypes: Seq[NetworkType] = _networkTypes.getOrElse(throw new PreconditionMissingException)

  def routeMap: RouteMap = _routeMap.getOrElse(throw new PreconditionMissingException)

  def ways: Seq[Way] = _ways.getOrElse(throw new PreconditionMissingException)

  def links: RouteLinks = _links.getOrElse(throw new PreconditionMissingException)

  def segments: Seq[RouteAnalysisSegment] = _segments.getOrElse(throw new PreconditionMissingException)

  def unexpectedNodeIds: Seq[Long] = _unexpectedNodeIds.getOrElse(throw new PreconditionMissingException)

  def unexpectedRelationIds: Seq[Long] = _unexpectedRelationIds.getOrElse(throw new PreconditionMissingException)

  def routeNameAnalysis: RouteNameAnalysis = _routeNameAnalysis.getOrElse(throw new PreconditionMissingException)

  def oldRouteNodeAnalysis: OldRouteNodeAnalysis = _oldRouteNodeAnalysis.getOrElse(throw new PreconditionMissingException)

  def nodes: RouteAnalysisNodes = _nodes.getOrElse(throw new PreconditionMissingException)

  def fragmentMap: FragmentMap = _fragmentMap.getOrElse(throw new PreconditionMissingException)

  def oldStructure: RouteStructure = _oldStructure.getOrElse(throw new PreconditionMissingException)

  def structure: Structure = _structure.getOrElse(throw new PreconditionMissingException)

  def routeMembers: Seq[RouteMember] = _routeMembers.getOrElse(throw new PreconditionMissingException)

  def geometryDigest: String = _geometryDigest.getOrElse(throw new PreconditionMissingException)

  def locationAnalysis: RouteLocationAnalysis = _locationAnalysis.getOrElse(throw new PreconditionMissingException)

  def tileAnalysis: RouteTileAnalysis = _tileAnalysis.getOrElse(throw new PreconditionMissingException)

  def oldRouteDetailAnalysis: RouteDetailAnalysis = {
    new RouteAnalysisBuilder(this).build
  }

  def oldToRouteData: RouteData = {
    RouteData(
      country,
      networkTypes.head,
      scopedNetworkType.networkScope,
      relation.toRaw,
      routeNameAnalysis.name.get,
      oldRouteNodeAnalysis.routeNodes.map(_.node),
      allWayNodes.get,
      ways.map(_.toRaw),
      Seq[RawRelation](), // TODO CHANGE add unexpected relations
      facts
    )
  }
}
