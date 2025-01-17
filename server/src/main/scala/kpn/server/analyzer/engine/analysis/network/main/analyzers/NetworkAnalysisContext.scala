package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkFact
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkShape
import kpn.api.custom.Day
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Timestamp
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.NetworkInfoNodeDetail
import kpn.core.doc.NetworkInfoRouteDetail
import kpn.core.doc.NodeDoc
import kpn.server.analyzer.engine.context.PreconditionMissingException

case class NetworkAnalysisContext(
  network: BaseNetworkDoc,
  analysisTimestamp: Timestamp,
  previousKnownCountry: Option[Country] = None,
  _scopedRouteTypeOption: Option[Option[ScopedRouteType]] = None,
  _country: Option[Option[Country]] = None,
  _name: Option[String] = None,
  _proposed: Option[Boolean] = None,
  facts: Seq[Fact] = Seq.empty,
  _nodeDocs: Option[Seq[NodeDoc]] = None,
  _networkFacts: Option[Seq[NetworkFact]] = None,
  _nodeDetails: Option[Seq[NetworkInfoNodeDetail]] = None,
  _routeDetails: Option[Seq[NetworkInfoRouteDetail]] = None,
  _extraNodeIds: Option[Seq[Long]] = None,
  _extraWayIds: Option[Seq[Long]] = None,
  _extraRelationIds: Option[Seq[Long]] = None,
  _km: Option[Long] = None,
  _meters: Option[Long] = None,
  _lastUpdated: Option[Option[Timestamp]] = None,
  _lastSurvey: Option[Option[Day]] = None,
  _brokenRouteCount: Option[Long] = None,
  _brokenRoutePercentage: Option[String] = None,
  _integrity: Option[Integrity] = None,
  _inaccessibleRouteCount: Option[Long] = None,
  _connectionCount: Option[Long] = None,
  _center: Option[Option[LatLonImpl]] = None,
  _shape: Option[Option[NetworkShape]] = None,
  abort: Boolean = false
) {

  def scopedRouteType: ScopedRouteType = {
    ScopedRouteType.from(
      network.routeType,
      network.routeScope,
    )
  }

  def withFact(fact: Fact): NetworkAnalysisContext = {
    copy(facts = facts :+ fact)
  }

  def scopedRouteTypeOption: Option[ScopedRouteType] = _scopedRouteTypeOption.getOrElse(throw new PreconditionMissingException)

  def country: Option[Country] = _country.getOrElse(throw new PreconditionMissingException)

  def name: String = _name.getOrElse(throw new PreconditionMissingException)

  def proposed: Boolean = _proposed.getOrElse(throw new PreconditionMissingException)

  def nodeDocs: Seq[NodeDoc] = _nodeDocs.getOrElse(throw new PreconditionMissingException)

  def networkFacts: Seq[NetworkFact] = _networkFacts.getOrElse(throw new PreconditionMissingException)

  def nodeDetails: Seq[NetworkInfoNodeDetail] = _nodeDetails.getOrElse(throw new PreconditionMissingException)

  def routeDetails: Seq[NetworkInfoRouteDetail] = _routeDetails.getOrElse(throw new PreconditionMissingException)

  def extraNodeIds: Seq[Long] = _extraNodeIds.getOrElse(throw new PreconditionMissingException)

  def extraWayIds: Seq[Long] = _extraWayIds.getOrElse(throw new PreconditionMissingException)

  def extraRelationIds: Seq[Long] = _extraRelationIds.getOrElse(throw new PreconditionMissingException)

  def km: Long = _km.getOrElse(throw new PreconditionMissingException)

  def meters: Long = _meters.getOrElse(throw new PreconditionMissingException)

  def lastUpdated: Option[Timestamp] = _lastUpdated.getOrElse(throw new PreconditionMissingException)

  def lastSurvey: Option[Day] = _lastSurvey.getOrElse(throw new PreconditionMissingException)

  def brokenRouteCount: Long = _brokenRouteCount.getOrElse(throw new PreconditionMissingException)

  def brokenRoutePercentage: String = _brokenRoutePercentage.getOrElse(throw new PreconditionMissingException)

  def integrity: Integrity = _integrity.getOrElse(throw new PreconditionMissingException)

  def inaccessibleRouteCount: Long = _inaccessibleRouteCount.getOrElse(throw new PreconditionMissingException)

  def connectionCount: Long = _connectionCount.getOrElse(throw new PreconditionMissingException)

  def center: Option[LatLonImpl] = _center.getOrElse(throw new PreconditionMissingException)

  def shape: Option[NetworkShape] = _shape.getOrElse(throw new PreconditionMissingException)
}

