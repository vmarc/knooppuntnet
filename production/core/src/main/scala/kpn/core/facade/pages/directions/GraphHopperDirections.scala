package kpn.core.facade.pages.directions

case class GraphHopperDirections(
  paths: Option[Seq[GraphHopperDirectionsPath]] = None
)
