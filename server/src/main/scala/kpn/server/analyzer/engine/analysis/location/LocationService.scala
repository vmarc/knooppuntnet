package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Language
import kpn.api.common.LocationInfo
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.location.Location
import kpn.api.custom.LocationKey

trait LocationService {
  def locationDefinition(locationId: String): Option[LocationDefinition]

  def name(language: Language, locationId: String): String

  def replaceNames(language: Language, routeLocationAnalysis: RouteLocationAnalysis): RouteLocationAnalysis

  def locationReplace(language: Language, location: Location): Location

  def toSubset(language: Language, locationKey: LocationKey): LocationSubset

  def toId(language: Language, location: String): String

  def toInfos(language: Language, all: Seq[String], locations: Seq[String]): Seq[LocationInfo]
}
