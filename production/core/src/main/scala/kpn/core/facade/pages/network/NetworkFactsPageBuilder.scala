package kpn.core.facade.pages.network

import kpn.shared.network.NetworkFactsPage

trait NetworkFactsPageBuilder {
  def build(networkId: Long): Option[NetworkFactsPage]
}
