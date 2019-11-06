package kpn.shared

object NetworkType {

  def withNewName(newName: String): Option[NetworkType] = {
    all.find(_.newName == newName)
  }

  val hiking = NetworkType("rwn", "hiking", "w", Seq("hiking", "walking", "foot"))
  val bicycle = NetworkType("rcn", "cycling", "c", Seq("bicycle"))
  val horseRiding = NetworkType("rhn", "horse-riding", "h", Seq("horse"))
  val canoe = NetworkType("rpn", "canoe", "p", Seq("canoe"))
  val motorboat = NetworkType("rmn", "motorboat", "m", Seq("motorboat"))
  val inlineSkates = NetworkType("rin", "inline-skating", "i", Seq("inline_skates"))

  val all: Seq[NetworkType] = Seq(hiking, bicycle, horseRiding, canoe, motorboat, inlineSkates)
}

case class NetworkType(name: String, newName: String, letter: String, routeTagValues: Seq[String]) {

  def nodeTagKey: String = name + "_ref"

  def expectedRouteRelationsTag: String = "expected_" + name + "_route_relations"

  override def toString: String = newName
}
