package kpn.server.api.analysis.pages.subset

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.subset.SubsetChangesPage
import kpn.api.custom.Subset

trait SubsetChangesPageBuilder {
  def build(user: Option[String], subset: Subset, parameters: ChangesParameters): Option[SubsetChangesPage]
}
