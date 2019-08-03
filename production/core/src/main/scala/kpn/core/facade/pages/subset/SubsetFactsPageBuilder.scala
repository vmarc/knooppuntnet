package kpn.core.facade.pages.subset

import kpn.shared.Subset
import kpn.shared.subset.SubsetFactsPage
import kpn.shared.subset.SubsetFactsPageNew

trait SubsetFactsPageBuilder {
  def build(subset: Subset): SubsetFactsPage
  def buildNew(subset: Subset): SubsetFactsPageNew
}
