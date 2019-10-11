package kpn.server.api.analysis.pages.network

import kpn.shared.network.NetworkMapPage

trait NetworkMapPageBuilder {
  def build(networkId: Long): Option[NetworkMapPage]
}
