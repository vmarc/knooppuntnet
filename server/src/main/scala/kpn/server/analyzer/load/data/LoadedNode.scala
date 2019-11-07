package kpn.server.analyzer.load.data

import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.data.Node
import kpn.shared.data.raw.RawNode

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
      case Some(c) => networkTypes.map(n => Subset(c, n))
      case None => Seq()
    }
  }
}
