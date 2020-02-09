package kpn.server.api.analysis.pages

import kpn.api.common.location.LocationPage
import kpn.api.custom.NetworkType

trait LocationPageBuilder {
  def build(networkType: NetworkType): Option[LocationPage]
}
