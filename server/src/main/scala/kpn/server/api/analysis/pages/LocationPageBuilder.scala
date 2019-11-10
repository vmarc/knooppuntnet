package kpn.server.api.analysis.pages

import kpn.api.common.LocationPage
import kpn.api.custom.NetworkType

trait LocationPageBuilder {
  def build(networkType: NetworkType): Option[LocationPage]
}
