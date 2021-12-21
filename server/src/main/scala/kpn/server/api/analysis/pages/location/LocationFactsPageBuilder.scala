package kpn.server.api.analysis.pages.location

import kpn.api.common.Language
import kpn.api.common.location.LocationFactsPage
import kpn.api.custom.LocationKey

trait LocationFactsPageBuilder {
  def build(language: Language, locationKey: LocationKey): Option[LocationFactsPage]
}
