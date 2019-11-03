package kpn.core.overpass

import kpn.shared.Subset

case class QuerySubsetNodeIds(subset: Subset) extends OverpassQuery {

  def name: String = s"node-ids-${subset.name}"

  def string: String = {
    s"area['admin_level'='2']['ISO3166-1'='${subset.country.domain.toUpperCase}'];" +
      s"(node['${subset.networkType.nodeTagKey}']['network:type'='node_network'](area););" +
      "out ids;"
  }
}
