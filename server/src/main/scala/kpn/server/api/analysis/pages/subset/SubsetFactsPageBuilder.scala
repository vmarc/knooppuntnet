package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetFactsPage
import kpn.api.custom.Subset

trait SubsetFactsPageBuilder {
  def build(subset: Subset): SubsetFactsPage
}
