package kpn.api.custom

import kpn.api.common.ScopedNetworkType

object NetworkType {

  def withName(name: String): Option[NetworkType] = {
    all.find(_.name == name)
  }

  val hiking = NetworkType("hiking", "w", Seq("hiking", "walking", "foot"))
  val bicycle = NetworkType("cycling", "c", Seq("bicycle"))
  val horseRiding = NetworkType("horse-riding", "h", Seq("horse"))
  val canoe = NetworkType("canoe", "p", Seq("canoe"))
  val motorboat = NetworkType("motorboat", "m", Seq("motorboat"))
  val inlineSkates = NetworkType("inline-skating", "i", Seq("inline_skates"))

  val all: Seq[NetworkType] = Seq(hiking, bicycle, horseRiding, canoe, motorboat, inlineSkates)
}

case class NetworkType(name: String, letter: String, routeTagValues: Seq[String]) {

  override def toString: String = name

  def scopedNetworkTypes: Seq[ScopedNetworkType] = ScopedNetworkType.all.filter(_.networkType == this)

}
