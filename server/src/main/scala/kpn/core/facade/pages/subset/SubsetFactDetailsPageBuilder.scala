package kpn.core.facade.pages.subset

import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.subset.SubsetFactDetailsPage

trait SubsetFactDetailsPageBuilder {
  def build(subset: Subset, fact: Fact): SubsetFactDetailsPage
}
