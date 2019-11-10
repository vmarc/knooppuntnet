package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkMapPage

trait NetworkMapPageBuilder {
  def build(networkId: Long): Option[NetworkMapPage]
}
