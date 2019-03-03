package kpn.shared.directions

case class DirectionsInstruction(
  node: Option[String] = None,
  text: Option[String] = None,
  streetName: Option[String] = None,
  distance: Option[Int] = None,
  sign: Option[String] = None,
  annotationText: Option[String] = None,
  annotationImportance: Option[Int] = None,
  exitNumber: Option[Int] = None,
  turnAngle: Option[Double] = None
)
