package kpn.server.repository

import kpn.api.common.common.Ref
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.views.location.LocationNodeView
import kpn.core.database.views.location.LocationView
import kpn.core.database.views.location.NodeRouteReferenceView
import org.springframework.stereotype.Component

@Component
class LocationRepositoryImpl(analysisDatabase: Database) extends LocationRepository {

  override def routesWithoutLocation(networkType: NetworkType): Seq[Ref] = {
    LocationView.query(analysisDatabase, "route-without-location", networkType, "").sortBy(_.name)
  }

  override def nodes(locationKey: LocationKey, parameters: LocationNodesParameters, stale: Boolean): Seq[LocationNodeInfo] = {
    val nodeInfos = LocationNodeView.query(analysisDatabase, locationKey, parameters, stale)
    nodeInfos.map { nodeInfo =>
      val routeReferences = NodeRouteReferenceView.query(analysisDatabase, locationKey.networkType, nodeInfo.id, stale)
      nodeInfo.copy(routeReferences = routeReferences)
    }
  }

  override def nodeCount(locationKey: LocationKey, stale: Boolean): Long = {
    LocationNodeView.queryCount(analysisDatabase, locationKey, stale)
  }
}
