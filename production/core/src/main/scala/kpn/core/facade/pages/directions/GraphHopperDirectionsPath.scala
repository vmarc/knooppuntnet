package kpn.core.facade.pages.directions

case class GraphHopperDirectionsPath(
  distance: Double = 0,
  weight: Double = 0,
  time: Long = 0,
  transfers: Int = 0,
  ascend: Double = 0,
  descend: Double = 0,
  instructions: Seq[GraphHopperDirectionsInstruction] = Seq()
)
