package kpn.core.facade.pages.network

import kpn.shared.network.NetworkNodesPage

trait NetworkNodesPageBuilder {
  def build(networkId: Long): Option[NetworkNodesPage]
}
