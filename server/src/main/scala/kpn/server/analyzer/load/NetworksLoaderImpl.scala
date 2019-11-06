package kpn.server.analyzer.load

import kpn.core.util.Log
import kpn.shared.ScopedNetworkType
import kpn.shared.Timestamp
import org.springframework.stereotype.Component

@Component
class NetworksLoaderImpl(
  networkIdsLoader: NetworkIdsLoader,
  networkInitialLoader: NetworkInitialLoader
) extends NetworksLoader {

  override def load(timestamp: Timestamp): Unit = {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      Log.context(scopedNetworkType.key) {
        load(timestamp, scopedNetworkType)
      }
    }
  }

  private def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Unit = {
    val networkIds = networkIdsLoader.load(timestamp, scopedNetworkType)
    networkInitialLoader.load(timestamp, networkIds)
  }
}
