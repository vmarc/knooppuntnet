package kpn.shared.directions

case class DirectionsPath(
  distance: Double = 0,
  weight: Double = 0,
  time: Long = 0,
  transfers: Int = 0,
  ascend: Double = 0,
  descend: Double = 0,
  instructions: Seq[DirectionsInstruction] = Seq()
)
