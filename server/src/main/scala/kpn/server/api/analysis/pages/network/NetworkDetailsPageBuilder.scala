package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkDetailsPage

trait NetworkDetailsPageBuilder {
  def build(networkId: Long): Option[NetworkDetailsPage]
}
