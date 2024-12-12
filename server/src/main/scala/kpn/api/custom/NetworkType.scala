package kpn.api.custom

object NetworkType {

  def withName(name: String): Option[NetworkType] = {
    all.find(_.name == name)
  }

  val hiking: NetworkType = NetworkType("hiking")
  val cycling: NetworkType = NetworkType("cycling")
  val horseRiding: NetworkType = NetworkType("horse-riding")
  val canoe: NetworkType = NetworkType("canoe")
  val motorboat: NetworkType = NetworkType("motorboat")
  val inlineSkating: NetworkType = NetworkType("inline-skating")

  val all: Seq[NetworkType] = Seq(hiking, cycling, horseRiding, canoe, motorboat, inlineSkating)
}

case class NetworkType(name: String) {

  override def toString: String = name

  def scopedNetworkTypes: Seq[ScopedNetworkType] = ScopedNetworkType.all.filter(_.networkType == this)

}
