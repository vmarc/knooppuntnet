package kpn.core.facade.pages.subset

import kpn.shared.Subset
import kpn.shared.subset.SubsetOrphanRoutesPage

trait SubsetOrphanRoutesPageBuilder {
  def build(subset: Subset): SubsetOrphanRoutesPage
}
