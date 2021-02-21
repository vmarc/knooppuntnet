package kpn.server.analyzer.load.data

import kpn.api.common.data.Node
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset

case class LoadedNode(country: Option[Country], networkTypes: Seq[NetworkType], name: String, node: Node) {

  def id: Long = node.id

  def subsets: Seq[Subset] = {
    country match {
      case Some(c) => networkTypes.map(n => kpn.api.custom.Subset(c, n))
      case None => Seq()
    }
  }
}
