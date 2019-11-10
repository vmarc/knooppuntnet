package kpn.server.repository

import kpn.api.common.common.Ref
import kpn.api.custom.NetworkType

trait LocationRepository {
  def routesWithoutLocation(networkType: NetworkType): Seq[Ref]
}
