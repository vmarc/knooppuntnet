package kpn.server.api.analysis.pages.subset

import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.subset.SubsetChangesPage

trait SubsetChangesPageBuilder {
  def build(user: Option[String], parameters: ChangesParameters): Option[SubsetChangesPage]
}
