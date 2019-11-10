package kpn.core.analysis

import kpn.api.common.data.Node
import kpn.api.common.data.Tagable
import kpn.api.custom.Country
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.node.NodeUtil

case class NetworkNode(node: Node, name: String, country: Option[Country]) extends Tagable {

  def id: Long = node.id

  def number: Option[Int] = NodeUtil.toNumber(name)

  def tags: Tags = node.tags
}
