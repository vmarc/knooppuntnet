package kpn.core.overpass

case class QueryNetherlandsNodes() extends OverpassQuery {

  def name: String = "netherlandsNodes"

  def string: String = """area(47796)->.searchArea;(node["rwn_ref"]["network:type"="node_network"](area.searchArea););out;"""
}
