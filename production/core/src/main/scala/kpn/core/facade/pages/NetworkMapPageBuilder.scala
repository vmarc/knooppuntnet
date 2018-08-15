package kpn.core.facade.pages

import kpn.shared.network.NetworkMapPage

trait NetworkMapPageBuilder {
  def build(networkId: Long): Option[NetworkMapPage]
}
