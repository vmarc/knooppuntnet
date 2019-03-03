package kpn.shared.directions

case class Directions(
  distance: Int = 0,
  ascend: Int = 0,
  descend: Int = 0,
  instructions: Seq[DirectionsInstruction] = Seq()
)
