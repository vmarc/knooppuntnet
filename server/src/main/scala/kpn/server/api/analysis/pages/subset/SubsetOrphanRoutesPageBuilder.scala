package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.custom.Subset

trait SubsetOrphanRoutesPageBuilder {
  def build(subset: Subset): SubsetOrphanRoutesPage
}
