package kpn.core.facade.pages

import kpn.shared.Subset
import kpn.shared.subset.SubsetOrphanRoutesPage

trait SubsetOrphanRoutesPageBuilder {
  def build(subset: Subset): SubsetOrphanRoutesPage
}
