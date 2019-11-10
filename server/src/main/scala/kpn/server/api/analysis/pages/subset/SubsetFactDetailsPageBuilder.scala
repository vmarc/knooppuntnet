package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetFactDetailsPage
import kpn.api.custom.Fact
import kpn.api.custom.Subset

trait SubsetFactDetailsPageBuilder {
  def build(subset: Subset, fact: Fact): SubsetFactDetailsPage
}
