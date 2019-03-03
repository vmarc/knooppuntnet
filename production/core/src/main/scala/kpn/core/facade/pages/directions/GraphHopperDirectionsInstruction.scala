package kpn.core.facade.pages.directions

case class GraphHopperDirectionsInstruction(
  text: Option[String] = None,
  streetName: Option[String] = None,
  distance: Option[Double] = None,
  time: Option[Int] = None,
  interval: Option[Seq[Int]] = None,
  sign: Option[Int] = None,
  annotationText: Option[String] = None,
  annotationImportance: Option[Int] = None,
  exitNumber: Option[Int] = None,
  turnAngle: Option[Double] = None
)
