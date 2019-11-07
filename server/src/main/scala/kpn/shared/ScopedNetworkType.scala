package kpn.shared

object ScopedNetworkType {

  val rwn: ScopedNetworkType = ScopedNetworkType(NetworkScope.regional, NetworkType.hiking)

  val rcn: ScopedNetworkType = ScopedNetworkType(NetworkScope.regional, NetworkType.bicycle)

  def apply(networkScope: NetworkScope, networkType: NetworkType): ScopedNetworkType = {
    val key = networkScope.letter + networkType.letter + "n"
    ScopedNetworkType(networkScope, networkType, key)
  }

  val all: Seq[ScopedNetworkType] = {
    NetworkType.all.flatMap { networkType =>
      NetworkScope.all.map(scope => ScopedNetworkType(scope, networkType))
    }
  }

  def withNetworkType(networkType: NetworkType): Seq[ScopedNetworkType] = {
    all.filter(_.networkType == networkType)
  }

}

case class ScopedNetworkType(networkScope: NetworkScope, networkType: NetworkType, key: String) {

  def nodeTagKey: String = key + "_ref"

  def expectedRouteRelationsTag: String = "expected_" + key + "_route_relations"

}
