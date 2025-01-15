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

case class NetworkAnalysisContext(
  network: BaseNetworkDoc,
  analysisTimestamp: Timestamp,
  previousKnownCountry: Option[Country] = None,
  scopedRouteTypeOption: Option[ScopedRouteType] = None,
  country: Option[Country] = None,
  name: String = "",
  proposed: Boolean = false,
  facts: Seq[Fact] = Seq.empty,
  nodeDocs: Seq[NodeDoc] = Seq.empty,
  networkFacts: Seq[NetworkFact] = Seq.empty,
  nodeDetails: Seq[NetworkInfoNodeDetail] = Seq.empty,
  routeDetails: Seq[NetworkInfoRouteDetail] = Seq.empty,
  extraNodeIds: Seq[Long] = Seq.empty,
  extraWayIds: Seq[Long] = Seq.empty,
  extraRelationIds: Seq[Long] = Seq.empty,
  km: Long = 0,
  meters: Long = 0,
  lastUpdated: Option[Timestamp] = None,
  lastSurvey: Option[Day] = None,
  brokenRouteCount: Long = 0,
  brokenRoutePercentage: String = "-",
  integrity: Integrity = Integrity(),
  inaccessibleRouteCount: Long = 0,
  connectionCount: Long = 0,
  center: Option[LatLonImpl] = None,
  shape: Option[NetworkShape] = None,
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
}

