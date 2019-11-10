package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.custom.Subset

trait SubsetNetworksPageBuilder {
  def build(subset: Subset): SubsetNetworksPage
}
