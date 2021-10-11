package kpn.core.analysis

import kpn.api.common.data.MetaData
import kpn.api.common.data.Node
import kpn.api.common.data.Tagable
import kpn.api.custom.Country
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.node.NodeUtil

case class NetworkNode(
  node: Node,
  name: String,
  longName: Option[String],
  country: Option[Country],
  locations: Seq[String]
) extends Tagable {

  def id: Long = node.id

  def number: Option[Int] = NodeUtil.toNumber(name)

  def tags: Tags = node.tags

  def toMeta: MetaData = {
    MetaData(
      node.version,
      node.timestamp,
      node.changeSetId
    )
  }
}
