package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.LocationKey

trait LocationNodesPageBuilder {
  def build(locationKey: LocationKey, parameters: LocationNodesParameters): Option[LocationNodesPage]
}
