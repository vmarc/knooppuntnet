package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.LatLon
import kpn.api.custom.Country
import kpn.api.custom.Relation

trait LocationAnalyzer {

  def countries(latLon: LatLon): Seq[Country]

  def country(latLons: Iterable[LatLon]): Option[Country]

  def relationCountry(relation: Relation): Option[Country]

}
