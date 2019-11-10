package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.analysis.NetworkNodeInfo
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.load.data.LoadedNode

object NodeInfoBuilder {

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
  ): NodeInfo = {
    NodeInfo(
      id,
      active,
      orphan,
      country,
      NodeAnalyzer.name(tags),
      NodeAnalyzer.names(tags),
      latitude,
      longitude,
      lastUpdated,
      tags,
      facts,
      None
    )
  }

  def fromLoadedNode(
    loadedNode: LoadedNode,
    active: Boolean = true,
    orphan: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo = {
    build(
      loadedNode.node.id,
      active,
      orphan,
      loadedNode.country,
      loadedNode.node.latitude,
      loadedNode.node.longitude,
      loadedNode.node.timestamp,
      loadedNode.node.tags,
      facts
    )
  }

  def fromNetworkNodeInfo(
    networkNodeInfo: NetworkNodeInfo,
    active: Boolean = true,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo = {
    val node = networkNodeInfo.networkNode.node
    build(
      node.id,
      active,
      orphan = false,
      networkNodeInfo.networkNode.country,
      node.latitude,
      node.longitude,
      node.timestamp,
      node.tags,
      facts
    )
  }

  def fromRawNode(
    rawNode: RawNode,
    country: Option[Country],
    active: Boolean = true,
    orphan: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo = {
    build(
      rawNode.id,
      active,
      orphan,
      country,
      rawNode.latitude,
      rawNode.longitude,
      rawNode.timestamp,
      rawNode.tags,
      facts
    )
  }
}
