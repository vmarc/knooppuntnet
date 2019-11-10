package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.custom.Subset

trait SubsetOrphanNodesPageBuilder {
  def build(subset: Subset): SubsetOrphanNodesPage
}
