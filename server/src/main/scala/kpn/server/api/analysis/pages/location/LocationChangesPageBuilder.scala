package kpn.server.api.analysis.pages.location

import kpn.api.common.Language
import kpn.api.common.location.LocationChangesPage
import kpn.api.common.location.LocationChangesParameters
import kpn.api.custom.LocationKey

trait LocationChangesPageBuilder {
  def build(language: Language, locationKey: LocationKey, parameters: LocationChangesParameters): Option[LocationChangesPage]
}
