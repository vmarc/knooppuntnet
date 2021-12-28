package kpn.api.common.changes.filter

import kpn.database.actions.statistics.ChangeSetCounts

object ChangesFilter {
  def empty: ChangesFilter = ChangesFilter(Seq.empty)

  def from(changeSetCounts: ChangeSetCounts, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    throw new RuntimeException("TODO")
    ChangesFilter(Seq.empty)

//    val periods = changeSetCounts.years.map { yearChangeSetCount =>
//      val monthPeriods = changeSetCounts.months.filter(_.year == yearChangeSetCount.year).map { monthChangeSetCount =>
//        val dayPeriods = changeSetCounts.days.filter(csc => csc.year == monthChangeSetCount.year && csc.month == monthChangeSetCount.month).map { dayChangeSetCount =>
//          ChangesFilterPeriod(
//            dayChangeSetCount.day,
//            dayChangeSetCount.total,
//            dayChangeSetCount.impact,
//            current = false,
//            selected = dayOption.contains(f"${dayChangeSetCount.day}%02d"),
//            Seq.empty
//          )
//        }
//        ChangesFilterPeriod(
//          monthChangeSetCount.month,
//          monthChangeSetCount.total,
//          monthChangeSetCount.impact,
//          current = false,
//          selected = monthOption.contains(f"${monthChangeSetCount.month}%02d"),
//          dayPeriods
//        )
//      }
//      ChangesFilterPeriod(
//        yearChangeSetCount.year,
//        yearChangeSetCount.total,
//        yearChangeSetCount.impact,
//        current = false,
//        selected = yearOption.contains(yearChangeSetCount.year.toString),
//        monthPeriods
//      )
//    }
//    ChangesFilter(periods)
  }
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
