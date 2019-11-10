package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkFactsPage

trait NetworkFactsPageBuilder {
  def build(networkId: Long): Option[NetworkFactsPage]
}
