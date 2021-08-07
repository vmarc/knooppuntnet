package kpn.server.api.analysis.pages.subset

import kpn.api.common.FactCount
import kpn.api.common.subset.SubsetFactsPage
import kpn.api.custom.Subset
import kpn.server.repository.StatisticsRepository
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetFactsPageBuilder(
  subsetRepository: SubsetRepository,
  overviewRepository: StatisticsRepository
) {

  def build(subset: Subset): SubsetFactsPage = {
    val subsetInfo = subsetRepository.subsetInfo(subset)
    //    val figures = overviewRepository.statisticValues()
    val factCounts: Seq[FactCount] = Seq.empty
    //  val factCounts = Fact.reportedFacts.flatMap { fact =>
    //    figures.get(fact.name + "Count").map { figure: Figure => FactCount(fact, figure.counts.getOrElse(subset, 0)) }
    //  }.filter(_.count > 0)
    SubsetFactsPage(subsetInfo, factCounts)
  }

}
