package kpn.core.facade.pages

import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.subset.SubsetChangesPage

trait SubsetChangesPageBuilder {
  def build(user: Option[String], parameters: ChangesParameters): Option[SubsetChangesPage]
}
