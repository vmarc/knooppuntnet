package kpn.core.load

import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.Timestamp

class NetworksLoaderImpl(
  networkIdsLoader: NetworkIdsLoader,
  networkInitialLoader: NetworkInitialLoader
) extends NetworksLoader {

  override def load(timestamp: Timestamp): Unit = {
    NetworkType.all.foreach { networkType =>
      Log.context(networkType.name) {
        load(timestamp, networkType)
      }
    }
  }

  private def load(timestamp: Timestamp, networkType: NetworkType): Unit = {
    val networkIds = networkIdsLoader.load(timestamp, networkType)
    networkInitialLoader.load(timestamp, networkIds)
  }
}
