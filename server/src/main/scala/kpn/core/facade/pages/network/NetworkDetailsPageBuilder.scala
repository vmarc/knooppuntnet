package kpn.core.facade.pages.network

import kpn.shared.network.NetworkDetailsPage

trait NetworkDetailsPageBuilder {
  def build(networkId: Long): Option[NetworkDetailsPage]
}
