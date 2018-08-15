package kpn.core.facade.pages

import kpn.shared.Subset
import kpn.shared.subset.SubsetFactsPage

trait SubsetFactsPageBuilder {
  def build(subset: Subset): SubsetFactsPage
}
