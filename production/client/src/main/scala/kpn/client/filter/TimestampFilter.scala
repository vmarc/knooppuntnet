// Migrated to Angular: timestamp-filter.ts
package kpn.client.filter

import japgolly.scalajs.react.Callback
import kpn.shared.TimeInfo
import kpn.shared.Timestamp

class TimestampFilter[T](
  kind: TimeFilterKind.Value,
  fun: (T) => Timestamp,
  timeInfo: TimeInfo,
  selectAll: Callback,
  selectLastWeek: Callback,
  selectLastMonth: Callback,
  selectLastYear: Callback,
  selectOlder: Callback
) extends Filter[T]("lastUpdated") {

  def passes(element: T): Boolean = {
    kind match {
      case TimeFilterKind.ALL => true
      case TimeFilterKind.LAST_WEEK => fun(element) >= timeInfo.lastWeekStart
      case TimeFilterKind.LAST_MONTH => fun(element) >= timeInfo.lastMonthStart
      case TimeFilterKind.LAST_YEAR => fun(element) >= timeInfo.lastYearStart
      case TimeFilterKind.OLDER => fun(element) < timeInfo.lastYearStart
    }
  }

  def filterOptions(allFilters: Filters[T], elements: Seq[T]): Option[FilterOptionGroup] = {
    if (elements.isEmpty) {
      None
    } else {
      val filteredElements = allFilters.filterExcept(elements, this)

      val allCount = filteredElements.size
      val lastWeekCount = filteredElements.count(e => fun(e) >= timeInfo.lastWeekStart)
      val lastMonthCount = filteredElements.count(e => fun(e) >= timeInfo.lastMonthStart)
      val lastYearCount = filteredElements.count(e => fun(e) >= timeInfo.lastYearStart)
      val olderCount = filteredElements.count(e => fun(e) < timeInfo.lastYearStart)

      val enableAll = !(lastWeekCount == allCount || lastMonthCount == allCount || lastYearCount == allCount || olderCount == allCount)

      val all = FilterOption("all", allCount, kind == TimeFilterKind.ALL, selectAll)
      val lastWeek = FilterOption("lastWeek", lastWeekCount, kind == TimeFilterKind.LAST_WEEK, selectLastWeek)
      val lastMonth = FilterOption("lastMonth", lastMonthCount, kind == TimeFilterKind.LAST_MONTH, selectLastMonth)
      val lastYear = FilterOption("lastYear", lastYearCount, kind == TimeFilterKind.LAST_YEAR, selectLastYear)
      val older = FilterOption("older", olderCount, kind == TimeFilterKind.OLDER, selectOlder)

      Some(FilterOptionGroup(name, all, lastWeek, lastMonth, lastYear, older))
    }
  }
}
