package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetMapPage
import kpn.api.custom.Subset

trait SubsetMapPageBuilder {
  def build(subset: Subset): SubsetMapPage
}
