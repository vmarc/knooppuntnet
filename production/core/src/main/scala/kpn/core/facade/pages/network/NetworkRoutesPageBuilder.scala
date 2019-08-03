package kpn.core.facade.pages.network

import kpn.shared.network.NetworkRoutesPage

trait NetworkRoutesPageBuilder {
  def build(networkId: Long): Option[NetworkRoutesPage]
}
