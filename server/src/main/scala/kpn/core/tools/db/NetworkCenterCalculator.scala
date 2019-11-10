package kpn.core.tools.db

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl
import kpn.core.analysis.Network

class NetworkCenterCalculator {

  def calculate(network: Network): Option[LatLonImpl] = {

    val latLons: Seq[LatLon] = {
      val nodeLatLons = network.nodes.map(_.networkNode.node)
      if (nodeLatLons.isEmpty) {
        val routeNodeLatLons = network.routes.flatMap(route => route.routeAnalysis.routeNodes.routeNodes.map(_.node))
        if (routeNodeLatLons.isEmpty) {
          network.routes.flatMap(route => route.routeAnalysis.allWayNodes)
        }
        else {
          routeNodeLatLons
        }
      }
      else {
        nodeLatLons
      }
    }

    if (latLons.isEmpty) {
      None
    }
    else {
      val lattitude = latLons.map(_.lat).sum / latLons.size
      val longititude = latLons.map(_.lon).sum / latLons.size
      Some(LatLonImpl(lattitude.toString, longititude.toString))
    }
  }

}
