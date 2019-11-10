package kpn.api.common.changes.filter

case class ChangesFilter(periods: Seq[ChangesFilterPeriod]) {

  def currentItemCount(impact: Boolean): Int = {
    currentPeriod match {
      case None =>
        if (impact) {
          impactedCount
        }
        else {
          totalCount
        }
      case Some(selectedPeriod) =>
        if (impact) {
          selectedPeriod.impactedCount
        }
        else {
          selectedPeriod.totalCount
        }
    }
  }

  def totalCount: Int = periods.map(_.totalCount).sum

  def impactedCount: Int = periods.map(_.impactedCount).sum

  def currentPeriod: Option[ChangesFilterPeriod] = {
    currentPeriodIn(periods)
  }

  private def currentPeriodIn(period: ChangesFilterPeriod): Option[ChangesFilterPeriod] = {
    if (period.current) {
      Some(period)
    }
    else {
      currentPeriodIn(period.periods)
    }
  }

  private def currentPeriodIn(periods: Seq[ChangesFilterPeriod]): Option[ChangesFilterPeriod] = {
    val xx = periods.map(p => currentPeriodIn(p))
    val yy = xx.find(_.isDefined).flatten
    yy
  }
}
