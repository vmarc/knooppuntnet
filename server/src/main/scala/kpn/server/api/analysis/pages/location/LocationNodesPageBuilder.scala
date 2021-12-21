package kpn.server.api.analysis.pages.location

import kpn.api.common.Language
import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.LocationKey

trait LocationNodesPageBuilder {

  def build(
    language: Language,
    locationKey: LocationKey,
    parameters: LocationNodesParameters
  ): Option[LocationNodesPage]

}
