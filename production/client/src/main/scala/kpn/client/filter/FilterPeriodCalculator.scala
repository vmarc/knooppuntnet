package kpn.client.filter

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

object FilterPeriodCalculator {

  private val yyyymmdd = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  private val dayOfWeek = WeekFields.of(Locale.FRANCE).dayOfWeek()

  def lastWeekStart: String = new FilterPeriodCalculator(LocalDate.now()).lastWeekStart

  def lastMonthStart: String = new FilterPeriodCalculator(LocalDate.now()).lastWeekStart

  def lastYearStart: String = new FilterPeriodCalculator(LocalDate.now()).lastWeekStart
}

class FilterPeriodCalculator(now: LocalDate) {

  def lastWeekStart: String = {
    now.`with`(FilterPeriodCalculator.dayOfWeek, 1).minusWeeks(1).format(FilterPeriodCalculator.yyyymmdd)
  }

  def lastMonthStart: String = now.withDayOfMonth(1).minusMonths(1).format(FilterPeriodCalculator.yyyymmdd)

  def lastYearStart: String = now.withDayOfYear(1).minusYears(1).format(FilterPeriodCalculator.yyyymmdd)
}
