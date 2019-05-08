package kpn.shared

object NetworkType {

  def withNetworkTagValue(networkTagValue: String): Option[NetworkType] = {
    all.find(_.networkTagValue == networkTagValue)
  }

  def withName(name: String): Option[NetworkType] = {
    all.find(_.name == name)
  }

  def withNewName(newName: String): Option[NetworkType] = {
    all.find(_.newName == newName)
  }

  val hiking = NetworkType("rwn", "Hiking", "hiking", Seq("hiking", "walking", "foot"))
  val bicycle = NetworkType("rcn", "Bicycle", "cycling", Seq("bicycle"))
  val horse = NetworkType("rhn", "Horse", "horse", Seq("horse"))
  val canoe = NetworkType("rpn", "Canoe", "canoe", Seq("canoe"))
  val motorboat = NetworkType("rmn", "Motorboat", "motorboat", Seq("motorboat"))
  val inlineSkates = NetworkType("rin", "Inline skates", "inline-skates", Seq("inline_skates"))

  val all = Seq(hiking, bicycle, horse, canoe, motorboat, inlineSkates)
}

case class NetworkType(name: String, title: String, newName: String, routeTagValues: Seq[String]) {
  def networkTagValue: String = name
  def nodeTagKey: String = name + "_ref"
  def expectedRouteRelationsTag: String = "expected_" + name + "_route_relations"
  override def toString: String = name
}
