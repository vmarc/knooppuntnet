package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkNodesPage

trait NetworkNodesPageBuilder {
  def build(networkId: Long): Option[NetworkNodesPage]
}
