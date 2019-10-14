package kpn.server.api.analysis.pages.subset

import kpn.shared.Subset
import kpn.shared.subset.SubsetFactsPage

trait SubsetFactsPageBuilder {
  def build(subset: Subset): SubsetFactsPage
}
