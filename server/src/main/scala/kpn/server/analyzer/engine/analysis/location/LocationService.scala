package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Language
import kpn.api.common.LocationInfo
import kpn.api.common.RouteLocationAnalysis
import kpn.api.custom.LocationKey

trait LocationService {
  def locationDefinition(locationId: String): Option[LocationDefinition]

  def name(language: Language, locationId: String): String

  def replaceNames(language: Language, routeLocationAnalysis: RouteLocationAnalysis): RouteLocationAnalysis

  def toId(language: Language, location: String): String

  def toSubset(language: Language, locationKey: LocationKey): LocationSubset

  def toInfos(language: Language, all: Seq[String], locations: Seq[String]): Seq[LocationInfo]
}
