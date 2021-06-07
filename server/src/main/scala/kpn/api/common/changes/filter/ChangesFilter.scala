package kpn.api.common.changes.filter

object ChangesFilter {
  def empty: ChangesFilter = ChangesFilter(Seq.empty)
}

case class ChangesFilter(periods: Seq[ChangesFilterPeriod]) {

  def currentItemCount(impact: Boolean): Long = {
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

  def totalCount: Long = periods.map(_.totalCount).sum

  def impactedCount: Long = periods.map(_.impactedCount).sum

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
