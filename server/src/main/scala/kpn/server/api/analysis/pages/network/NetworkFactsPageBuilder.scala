package kpn.server.api.analysis.pages.network

import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.OldNetworkFactsPage

trait NetworkFactsPageBuilder {
  def build(networkId: Long): Option[NetworkFactsPage]
  def oldBuild(networkId: Long): Option[OldNetworkFactsPage]
}
