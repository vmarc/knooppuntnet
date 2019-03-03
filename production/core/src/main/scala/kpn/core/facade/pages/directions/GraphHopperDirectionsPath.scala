package kpn.core.facade.pages.directions

case class GraphHopperDirectionsPath(
  distance: Double = 0,
  ascend: Double = 0,
  descend: Double = 0,
  instructions: Seq[GraphHopperDirectionsInstruction] = Seq()
)
