package kpn.api.custom

object ScopedNetworkType {

  val rwn: ScopedNetworkType = ScopedNetworkType(NetworkScope.regional, NetworkType.hiking)
  val rcn: ScopedNetworkType = ScopedNetworkType(NetworkScope.regional, NetworkType.cycling)
  val rmn: ScopedNetworkType = ScopedNetworkType(NetworkScope.regional, NetworkType.motorboat)
  val lwn: ScopedNetworkType = ScopedNetworkType(NetworkScope.local, NetworkType.hiking)
  val lcn: ScopedNetworkType = ScopedNetworkType(NetworkScope.local, NetworkType.cycling)
  val lpn: ScopedNetworkType = ScopedNetworkType(NetworkScope.local, NetworkType.canoe)

  def apply(networkScope: NetworkScope, networkType: NetworkType): ScopedNetworkType = {
    val key = networkScope.letter + networkType.letter + "n"
    ScopedNetworkType(networkScope, networkType, key)
  }

  val all: Seq[ScopedNetworkType] = {
    NetworkType.all.flatMap { networkType =>
      NetworkScope.all.map(scope => ScopedNetworkType(scope, networkType))
    }
  }

  def withKey(key: String): Option[ScopedNetworkType] = {
    all.find(_.key == key)
  }

  def withNetworkType(networkType: NetworkType): Seq[ScopedNetworkType] = {
    all.filter(_.networkType == networkType)
  }

  def from(networkScope: NetworkScope, networkType: NetworkType): ScopedNetworkType = {
    all.find(ns => ns.networkType == networkType && ns.networkScope == networkScope).get
  }

}

case class ScopedNetworkType(networkScope: NetworkScope, networkType: NetworkType, key: String) {

  override def toString: String = key

  def nodeRefTagKey: String = key + "_ref"

  def nodeNameTagKey: String = key + "_name"

  def expectedRouteRelationsTag: String = "expected_" + key + "_route_relations"

}
