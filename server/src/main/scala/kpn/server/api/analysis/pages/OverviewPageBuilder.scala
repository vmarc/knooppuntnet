package kpn.server.api.analysis.pages

import kpn.api.common.Language
import kpn.api.common.statistics.StatisticValues

trait OverviewPageBuilder {
  def build(language: Language): Option[Seq[StatisticValues]]
}
