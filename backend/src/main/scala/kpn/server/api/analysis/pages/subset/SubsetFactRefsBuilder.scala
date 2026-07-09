package kpn.server.api.analysis.pages.subset

import kpn.api.common.Fact
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.server.repository.FactRefRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class SubsetFactRefsBuilder(factRefRepository: FactRefRepository) {

  def build(subset: Subset, fact: Fact): SubsetFactRefs = {
    factRefRepository.factRefs(subset, fact)
  }
}
