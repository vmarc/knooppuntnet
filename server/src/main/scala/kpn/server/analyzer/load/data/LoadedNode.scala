package kpn.server.analyzer.load.data

import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer

object LoadedNode {
  def from(country: Option[Country], node: RawNode): LoadedNode = {
    val networkTypes = NodeAnalyzer.networkTypes(node.tags)
    val name = NodeAnalyzer.name(node.tags)
    LoadedNode(country, networkTypes, name, Node(node))
  }
}

case class LoadedNode(country: Option[Country], networkTypes: Seq[NetworkType], name: String, node: Node) {

  def id: Long = node.id

  def subsets: Seq[Subset] = {
    country match {
      case Some(c) => networkTypes.map(n => kpn.api.custom.Subset(c, n))
      case None => Seq.empty
    }
  }
}
