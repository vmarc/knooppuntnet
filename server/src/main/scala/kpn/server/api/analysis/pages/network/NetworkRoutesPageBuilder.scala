package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkRoutesPage

trait NetworkRoutesPageBuilder {
  def build(networkId: Long): Option[NetworkRoutesPage]
}
