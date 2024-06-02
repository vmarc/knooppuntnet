package kpn.server.api.analysis.pages

import kpn.api.common.Language
import kpn.api.common.location.LocationsPage
import kpn.api.custom.Subset

trait LocationsPageBuilder {
  def build(language: Language, subset: Subset): Option[LocationsPage]
}
