package kpn.core.facade.pages.network

import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkSummary

object NetworkSummaryBuilder {

  def toSummary(networkInfo: NetworkInfo, changeCount: Int): NetworkSummary = {

    val nodeCount = networkInfo.detail match {
      case Some(detail) => detail.nodes.size
      case None => 0
    }

    val routeCount = networkInfo.detail match {
      case Some(detail) => detail.routes.size
      case None => 0
    }

    NetworkSummary(
      networkInfo.attributes.networkType,
      networkInfo.attributes.name,
      networkInfo.factCount,
      nodeCount,
      routeCount,
      changeCount
    )
  }
}
