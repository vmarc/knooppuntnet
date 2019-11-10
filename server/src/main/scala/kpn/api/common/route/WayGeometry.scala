package kpn.api.common.route

import kpn.api.common.LatLonImpl
import kpn.api.common.diff.RouteData

object WayGeometry {

  def from(routeData: RouteData): Seq[WayGeometry] = {
    routeData.ways.map { way =>
      val nodes = way.nodeIds.flatMap { nodeId =>
        routeData.nodes.find(_.id == nodeId)
      }
      // TODO could add logic here to combine adjecent ways into combined WayGeometries
      WayGeometry(way.id, nodes.map(n => LatLonImpl(n.latitude, n.longitude)))
    }
  }
}

case class WayGeometry(id: Long, nodes: Seq[LatLonImpl])
