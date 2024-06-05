package kpn.server.api.analysis.pages.location

import kpn.api.common.Language
import kpn.api.common.location.LocationDetailsPage
import kpn.api.custom.LocationKey

trait LocationDetailsPageBuilder {
  def build(language: Language, locationKey: LocationKey): Option[LocationDetailsPage]
}
