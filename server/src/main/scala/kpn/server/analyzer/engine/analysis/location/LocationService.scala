package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Language
import kpn.api.common.RouteLocationAnalysis
import kpn.api.custom.LocationKey

trait LocationService {

  def locationDefinition(locationId: String): Option[LocationDefinition]

  def name(language: Language, locationId: String): String

  def replaceNames(language: Language, routeLocationAnalysis: RouteLocationAnalysis): RouteLocationAnalysis

  def translate(language: Language, locationKeyParam: LocationKey): LocationKey

}
