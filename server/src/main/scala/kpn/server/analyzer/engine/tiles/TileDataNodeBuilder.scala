package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.network.NetworkNodeInfo2
import kpn.api.custom.Fact
import kpn.api.custom.FactLevel
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

class TileDataNodeBuilder {

  def build(networkType: NetworkType, node: NodeInfo): TileDataNode = {
    TileDataNode(
      node.id,
      node.name(networkType),
      node.latitude,
      node.longitude,
      layer(node.orphan, node.facts)
    )
  }

  def build(node: NetworkNodeInfo2): TileDataNode = {
    TileDataNode(
      node.id,
      node.name,
      node.latitude,
      node.longitude,
      layer(isOrphan(node), node.facts)
    )
  }

  private def layer(orphan: Boolean, facts: Seq[Fact]): String = {
    if (orphan) {
      if (hasError(facts)) {
        "error-orphan-node"
      }
      else {
        "orphan-node"
      }
    }
    else if (hasError(facts)) {
      "error-node"
    }
    else {
      "node"
    }
  }

  private def isOrphan(node: NetworkNodeInfo2): Boolean = {
    !node.definedInRelation && node.routeReferences.isEmpty
  }

  private def hasError(facts: Seq[Fact]): Boolean = {
    facts.exists(_.level == FactLevel.ERROR)
  }

}
