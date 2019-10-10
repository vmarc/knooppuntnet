package kpn.core.facade.pages.subset

import kpn.shared.Subset
import kpn.shared.subset.SubsetOrphanNodesPage

trait SubsetOrphanNodesPageBuilder {
  def build(subset: Subset): SubsetOrphanNodesPage
}
