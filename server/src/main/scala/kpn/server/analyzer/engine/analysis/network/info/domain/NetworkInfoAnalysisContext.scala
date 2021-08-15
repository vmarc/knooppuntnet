package kpn.server.analyzer.engine.analysis.network.info.domain

import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkFact
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkShape
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.mongo.doc.NetworkDoc
import kpn.core.mongo.doc.NodeDoc

case class NetworkInfoAnalysisContext(
  analysisTimestamp: Timestamp,
  networkDoc: NetworkDoc,
  scopedNetworkTypeOption: Option[ScopedNetworkType] = None,
  country: Option[Country] = None,
  name: String = "",
  nodes: Seq[NodeDoc] = Seq.empty,
  facts: Seq[Fact] = Seq.empty,
  networkFacts: Seq[NetworkFact] = Seq.empty,
  nodeDetails: Seq[NetworkNodeDetail] = Seq.empty,
  routeDetails: Seq[NetworkRouteDetail] = Seq.empty,
  routes: Seq[NetworkRouteRow] = Seq.empty, // TODO MONGO use better name
  extraNodeIds: Seq[Long] = Seq.empty,
  extraWayIds: Seq[Long] = Seq.empty,
  extraRelationIds: Seq[Long] = Seq.empty,
  nodeIds: Seq[Long] = Seq.empty, // contains both ids of nodes in network relation and route relations and ways // TODO MONGO still needed after NetworkUpdater implementation?
  changeCount: Long = 0,
  km: Long = 0,
  meters: Long = 0,
  lastUpdated: Option[Timestamp] = None,
  lastSurvey: Option[Day] = None,
  brokenRouteCount: Long = 0,
  brokenRoutePercentage: String = "-",
  integrity: Integrity = Integrity(),
  unaccessibleRouteCount: Long = 0,
  connectionCount: Long = 0,
  center: Option[LatLonImpl] = None,
  shape: Option[NetworkShape] = None,
  abort: Boolean = false
) {

  def scopedNetworkType: ScopedNetworkType = {
    scopedNetworkTypeOption.getOrElse {
      throw new IllegalArgumentException("trying to use scopedNetworkType before definition")
    }
  }

  def withFact(fact: Fact): NetworkInfoAnalysisContext = {
    copy(facts = facts :+ fact)
  }

}
