package kpn.core.facade.pages

import kpn.shared.LocationPage
import kpn.shared.NetworkType

trait LocationPageBuilder {
  def build(networkType: NetworkType): Option[LocationPage]
}
