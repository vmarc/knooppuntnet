package kpn.server.repository

import kpn.api.common.common.Ref
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType

trait LocationRepository {

  def routesWithoutLocation(networkType: NetworkType): Seq[Ref]

  def nodes(locationKey: LocationKey, parameters: LocationNodesParameters, stale: Boolean = true): Seq[LocationNodeInfo]

  def nodeCount(locationKey: LocationKey, stale: Boolean = true): Long

}
