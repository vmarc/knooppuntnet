package kpn.server.api.analysis.pages.subset

import kpn.core.app.stats.Figure
import kpn.core.db.couch.Couch
import kpn.server.repository.OverviewRepository
import kpn.shared.Fact
import kpn.shared.FactCount
import kpn.shared.Subset
import kpn.shared.subset.SubsetFactsPage
import org.springframework.stereotype.Component

@Component
class SubsetFactsPageBuilderImpl(
  overviewRepository: OverviewRepository
) extends SubsetFactsPageBuilder {

  override def build(subset: Subset): SubsetFactsPage = {
    val figures = overviewRepository.figures(Couch.uiTimeout)
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
    val factCounts = Fact.reportedFacts.flatMap { fact =>
      figures.get(fact.name + "Count").map { figure: Figure => FactCount(fact, figure.value(subset)) }
    }.filter(_.count > 0)
    SubsetFactsPage(subsetInfo, factCounts)
  }

}
