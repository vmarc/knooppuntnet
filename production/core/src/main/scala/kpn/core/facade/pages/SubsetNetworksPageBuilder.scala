package kpn.core.facade.pages

import kpn.shared.Subset
import kpn.shared.subset.SubsetNetworksPage

trait SubsetNetworksPageBuilder {
  def build(subset: Subset): SubsetNetworksPage
}
