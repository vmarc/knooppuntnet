package kpn.core.overpass

import kpn.shared.NetworkType

case class QueryNodeIds(networkType: NetworkType) extends OverpassQuery {

  def name: String = s"node-ids-${networkType.name}"

  def string: String = {
    s"node['${networkType.nodeTagKey}'];out ids;"
  }
}
