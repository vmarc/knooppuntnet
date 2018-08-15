package kpn.core.overpass

import kpn.shared.Subset

case class QueryRoutes(subset: Subset) extends OverpassQuery {

  def name: String = s"routes-${subset.name}"

  def string: String = {
    s"area['admin_level'='2']['ISO3166-1'='${subset.country.domain.toUpperCase}'];" +
      s"(relation['type'='route']['network'='${subset.networkType.name}'](area););" +
      "out meta;"
  }
}
