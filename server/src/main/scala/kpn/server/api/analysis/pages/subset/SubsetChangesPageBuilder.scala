package kpn.server.api.analysis.pages.subset

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.subset.SubsetChangesPage

trait SubsetChangesPageBuilder {
  def build(user: Option[String], parameters: ChangesParameters): Option[SubsetChangesPage]
}
