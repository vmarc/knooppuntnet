package kpn.core.directions

import kpn.core.facade.pages.directions.GraphHopperDirections

trait DirectionsEngine {
  def get(language: String, gpx: String): Option[GraphHopperDirections]
}
