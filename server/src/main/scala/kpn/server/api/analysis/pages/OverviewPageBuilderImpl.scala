package kpn.server.api.analysis.pages

import kpn.api.common.FR
import kpn.api.common.Language
import kpn.api.common.statistics.StatisticValue
import kpn.api.common.statistics.StatisticValues
import kpn.api.custom.Subset
import kpn.core.util.Formatter
import kpn.database.actions.statistics.StatisticLongValues
import kpn.server.repository.StatisticsRepository
import org.springframework.stereotype.Component

@Component
class OverviewPageBuilderImpl(
  statisticsRepository: StatisticsRepository,
) extends OverviewPageBuilder {

  override def build(language: Language): Option[Seq[StatisticValues]] = {
    val values = statisticsRepository.statisticValues()
    Some(
      Seq(
        toStaticValues(language, values),
        integrityCheckPassRate(values),
        integrityCheckCoverage(values)
      ).flatten
    )
  }

  private def toStaticValues(language: Language, values: Seq[StatisticLongValues]): Seq[StatisticValues] = {
    values.map { statisticLongValues =>
      StatisticValues(
        statisticLongValues._id,
        formatted(language, statisticLongValues.values.map(_.value).sum),
        statisticLongValues.values.map(statisticLongValue =>
          StatisticValue(
            statisticLongValue.country,
            statisticLongValue.networkType,
            formatted(language, statisticLongValue.value)
          )
        )
      )
    }
  }

  private def integrityCheckPassRate(values: Seq[StatisticLongValues]): Seq[StatisticValues] = {
    values.find(_._id == "IntegrityCheckCount").toSeq.flatMap { integrityCheckCount =>
      values.find(_._id == "IntegrityCheckFailedCount").toSeq.map { integrityCheckFailedCount =>
        val total = Formatter.percentage(integrityCheckCount.total() - integrityCheckFailedCount.total(), integrityCheckCount.total())
        val longs = Subset.all.map { subset =>
          val subsetIntegrityCheckCount = integrityCheckCount.subsetValue(subset)
          val subsetIntegrityCheckFailedCount = integrityCheckFailedCount.subsetValue(subset)
          val value = Formatter.percentage(subsetIntegrityCheckCount - subsetIntegrityCheckFailedCount, subsetIntegrityCheckCount)
          StatisticValue(
            subset.country,
            subset.networkType,
            value
          )
        }
        StatisticValues(
          "IntegrityCheckPassRate",
          total,
          longs
        )
      }
    }
  }

  private def integrityCheckCoverage(values: Seq[StatisticLongValues]): Seq[StatisticValues] = {
    values.find(_._id == "NodeCount").toSeq.flatMap { nodeCount =>
      values.find(_._id == "IntegrityCheckCount").toSeq.map { integrityCheckCount =>
        val total = Formatter.percentage(integrityCheckCount.total(), nodeCount.total())
        val longs = Subset.all.map { subset =>
          val subsetNodeCount = nodeCount.subsetValue(subset)
          val subsetIntegrityCheckCount = integrityCheckCount.subsetValue(subset)
          val value = Formatter.percentage(subsetIntegrityCheckCount, subsetNodeCount)
          StatisticValue(
            subset.country,
            subset.networkType,
            value
          )
        }
        StatisticValues(
          "IntegrityCheckCoverage",
          total,
          longs
        )
      }
    }
  }

  private def formatted(language: Language, value: Long): String = {
    val f = Formatter.number(value)
    if (FR == language) f.replaceAll(".", " ") else f
  }
}
