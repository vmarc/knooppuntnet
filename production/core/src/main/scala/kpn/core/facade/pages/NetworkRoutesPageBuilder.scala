package kpn.core.facade.pages

import kpn.shared.network.NetworkRoutesPage

trait NetworkRoutesPageBuilder {
  def build(networkId: Long): Option[NetworkRoutesPage]
}
