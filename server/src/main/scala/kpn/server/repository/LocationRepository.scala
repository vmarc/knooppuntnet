package kpn.server.repository

import kpn.shared.NetworkType
import kpn.shared.common.Ref

trait LocationRepository {
  def routesWithoutLocation(networkType: NetworkType): Seq[Ref]
}
