package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.server.repository.FactRefRepository
import org.springframework.stereotype.Component

@Component
class SubsetFactRefsBuilder(factRefRepository: FactRefRepository) {

  def build(subset: Subset, fact: Fact): SubsetFactRefs = {
    factRefRepository.factRefs(subset, fact)
  }
}
