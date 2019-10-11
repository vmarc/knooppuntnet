package kpn.server.api.analysis.pages.network

import kpn.shared.network.NetworkRoutesPage

trait NetworkRoutesPageBuilder {
  def build(networkId: Long): Option[NetworkRoutesPage]
}
