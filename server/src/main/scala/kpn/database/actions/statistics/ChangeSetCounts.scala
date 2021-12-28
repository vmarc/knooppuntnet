package kpn.database.actions.statistics

import kpn.api.common.changes.filter.ChangesFilterOption

case class ChangeSetCounts(
  years: Seq[ChangeSetCount2] = Seq.empty,
  months: Seq[ChangeSetCount2] = Seq.empty,
  days: Seq[ChangeSetCount2] = Seq.empty,
) {

  def toFilterOptions(yearOption: Option[Long], monthOption: Option[Long], dayOption: Option[Long]): Seq[ChangesFilterOption] = {
    years.flatMap { yearCount =>
      val monthFilterOptions = months.flatMap { monthCount =>
        val dayFilterOptions = days.map { dayCount =>
          ChangesFilterOption(
            level = "day",
            name = dayCount.day.toString,
            year = yearCount.year,
            month = Some(monthCount.month),
            day = Some(dayCount.day),
            totalCount = dayCount.total,
            impactedCount = dayCount.impact,
            current = dayOption.contains(dayCount.day)
          )
        }
        val monthFilterOption = ChangesFilterOption(
          level = "month",
          name = monthCount.month.toString,
          year = yearCount.year,
          month = Some(monthCount.month),
          day = None,
          totalCount = monthCount.total,
          impactedCount = monthCount.impact,
          current = dayOption.isEmpty && monthOption.contains(monthCount.month)
        )
        monthFilterOption +: dayFilterOptions
      }

      val yearFilterOption = ChangesFilterOption(
        level = "year",
        name = yearCount.year.toString,
        year = yearCount.year,
        month = None,
        day = None,
        totalCount = yearCount.total,
        impactedCount = yearCount.impact,
        current = monthOption.isEmpty && yearOption.contains(yearCount.year)
      )
      yearFilterOption +: monthFilterOptions
    }
  }
}
