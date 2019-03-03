package kpn.core.facade.pages

import kpn.shared.directions.Directions

trait DirectionsBuilder {
  def build(language: String, exampleName: String): Option[Directions]
}
