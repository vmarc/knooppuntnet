package kpn.core.repository

import kpn.core.analysis.NetworkNodeInfo
import kpn.core.load.data.LoadedNode
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NodeInfo
import kpn.shared.Timestamp
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawNode
import kpn.shared.node.NodeNameAnalyzer.name
import kpn.shared.node.NodeNameAnalyzer.rcnName
import kpn.shared.node.NodeNameAnalyzer.rhnName
import kpn.shared.node.NodeNameAnalyzer.rinName
import kpn.shared.node.NodeNameAnalyzer.rmnName
import kpn.shared.node.NodeNameAnalyzer.rpnName
import kpn.shared.node.NodeNameAnalyzer.rwnName

object NodeInfoBuilder {

  def build(
    id: Long,
    active: Boolean,
    display: Boolean,
    ignored: Boolean,
    orphan: Boolean,
    country: Option[Country],
    latitude: String,
    longitude: String,
    lastUpdated: Timestamp,
    tags: Tags,
    facts: Seq[Fact]
  ): NodeInfo = {

    val allFacts = if (!tags.has("network:type", "node_network")) {
      if (!facts.contains(Fact.NodeNetworkTypeNotTagged)) {
        facts ++ Seq(Fact.NodeNetworkTypeNotTagged)
      }
      else {
        facts
      }
    }
    else {
      facts
    }

    NodeInfo(
      id,
      active,
      display,
      ignored,
      orphan,
      country,
      name(tags),
      rcnName(tags),
      rwnName(tags),
      rhnName(tags),
      rmnName(tags),
      rpnName(tags),
      rinName(tags),
      latitude,
      longitude,
      lastUpdated,
      tags,
      allFacts
    )
  }

  def fromLoadedNode(loadedNode: LoadedNode, active: Boolean = true, display: Boolean = true, ignored: Boolean = false, orphan: Boolean = false,
    facts: Seq[Fact] = Seq.empty)
  : NodeInfo = {
    build(
      loadedNode.node.id,
      active,
      display,
      ignored,
      orphan,
      loadedNode.country,
      loadedNode.node.latitude,
      loadedNode.node.longitude,
      loadedNode.node.timestamp,
      loadedNode.node.tags,
      facts
    )
  }

  def fromNetworkNodeInfo(networkNodeInfo: NetworkNodeInfo, active: Boolean = true, facts: Seq[Fact] = Seq.empty): NodeInfo = {
    val node = networkNodeInfo.networkNode.node
    build(
      node.id,
      active,
      display = true,
      ignored = false,
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
    display: Boolean = true,
    ignored: Boolean = false,
    orphan: Boolean = false,
    facts: Seq[Fact] = Seq.empty
  ): NodeInfo = {
    build(
      rawNode.id,
      active,
      display,
      ignored,
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
