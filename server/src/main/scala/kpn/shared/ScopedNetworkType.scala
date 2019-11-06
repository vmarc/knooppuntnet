package kpn.shared

object ScopedNetworkType {

  def apply(networkScope: NetworkScope, networkType: NetworkType): ScopedNetworkType = {
    val key = networkScope.letter + networkType.letter + "n"
    ScopedNetworkType(networkScope, networkType, key)
  }

  val all: Seq[ScopedNetworkType] = {
    NetworkType.all.flatMap { networkType =>
      NetworkScope.all.map(scope => ScopedNetworkType(scope, networkType))
    }
  }
}

case class ScopedNetworkType(networkScope: NetworkScope, networkType: NetworkType, key: String) {
  def nodeTagKey: String = key + "_ref"
}
