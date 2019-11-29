package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.analysis.NetworkNodeInfo
import kpn.server.analyzer.load.data.LoadedNode

trait NodeInfoBuilder {

  def build(
    id: Long,
    active: Boolean,
    orphan: Boolean,
    country: Option[Country],
    latitude: String,
    longitude: String,
    lastUpdated: Timestamp,
    tags: Tags,
    facts: Seq[Fact]
  ): NodeInfo

  def fromLoadedNode(
    loadedNode: LoadedNode,
    active: Boolean = true,
    orphan: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo

  def fromNetworkNodeInfo(
    networkNodeInfo: NetworkNodeInfo,
    active: Boolean = true,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo

  def fromRawNode(
    rawNode: RawNode,
    country: Option[Country],
    active: Boolean = true,
    orphan: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo
}
