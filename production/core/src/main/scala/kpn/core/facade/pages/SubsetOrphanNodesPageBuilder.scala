package kpn.core.facade.pages

import kpn.shared.ApiResponse
import kpn.shared.Subset
import kpn.shared.subset.SubsetOrphanNodesPage

trait SubsetOrphanNodesPageBuilder {
  def build(subset: Subset): SubsetOrphanNodesPage
}
