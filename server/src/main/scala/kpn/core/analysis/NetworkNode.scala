package kpn.core.analysis

import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.shared.data.Node
import kpn.shared.data.Tagable
import kpn.shared.Country
import kpn.shared.data.Tags

case class NetworkNode(node: Node, name: String, country: Option[Country]) extends Tagable {

  def id: Long = node.id

  def number: Option[Int] = NodeUtil.toNumber(name)

  def tags: Tags = node.tags
}
