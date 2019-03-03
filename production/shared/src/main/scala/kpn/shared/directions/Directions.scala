package kpn.shared.directions

case class Directions(
  paths: Option[Seq[DirectionsPath]] = None
)
