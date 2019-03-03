package kpn.core.directions

import kpn.shared.directions.Directions

trait DirectionsEngine {
  def get(language: String, gpx: String): Option[Directions]
}
