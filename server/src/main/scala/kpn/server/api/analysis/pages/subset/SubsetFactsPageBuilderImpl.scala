package kpn.server.api.analysis.pages.subset

import kpn.api.common.FactCount
import kpn.api.common.subset.SubsetFactsPage
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.app.stats.Figure
import kpn.core.db.couch.Couch
import kpn.server.repository.OverviewRepository
import org.springframework.stereotype.Component

@Component
class SubsetFactsPageBuilderImpl(
  overviewRepository: OverviewRepository
) extends SubsetFactsPageBuilder {

  override def build(subset: Subset): SubsetFactsPage = {
    val figures = overviewRepository.figures(Couch.uiTimeout)
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
    val factCounts = Fact.reportedFacts.flatMap { fact =>
      figures.get(fact.name + "Count").map { figure: Figure => FactCount(fact, figure.counts.getOrElse(subset, 0)) }
    }.filter(_.count > 0)
    SubsetFactsPage(subsetInfo, factCounts)
  }

}
