package kpn.server.api.analysis.pages.subset

import kpn.shared.Subset
import kpn.shared.subset.SubsetOrphanRoutesPage

trait SubsetOrphanRoutesPageBuilder {
  def build(subset: Subset): SubsetOrphanRoutesPage
}
