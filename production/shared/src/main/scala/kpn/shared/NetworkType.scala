package kpn.shared

object NetworkType {

  def withNetworkTagValue(networkTagValue: String): Option[NetworkType] = {
    all.find(_.networkTagValue == networkTagValue)
  }

  def withName(name: String): Option[NetworkType] = {
    all.find(_.name == name)
  }

  val hiking = NetworkType("rwn", "Hiking", Seq("hiking", "walking", "foot"))
  val bicycle = NetworkType("rcn", "Bicycle", Seq("bicycle"))
  val horse = NetworkType("rhn", "Horse", Seq("horse"))
  val canoe = NetworkType("rpn", "Canoe", Seq("canoe"))
  val motorboat = NetworkType("rmn", "Motorboat", Seq("motorboat"))

  val all = Seq(hiking, bicycle, horse, canoe, motorboat)
}

case class NetworkType(name: String, title: String, routeTagValues: Seq[String]) {
  val networkTagValue: String = name
  val nodeTagKey: String = name + "_ref"

  override def toString: String = name
}
