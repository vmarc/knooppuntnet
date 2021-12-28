package kpn.api.common.changes.filter

object ChangesFilterOption {

  def changesCount(filterOptions: Seq[ChangesFilterOption], parameters: ChangesParameters): Long = {
    if (filterOptions.nonEmpty) {
      val filterOption = filterOptions.find(_.current).getOrElse(filterOptions.head)
      if (parameters.impact) {
        filterOption.impactedCount
      }
      else {
        filterOption.totalCount
      }
    }
    else {
      0L
    }
  }
}

case class ChangesFilterOption(
  level: String,
  name: String,
  year: Long,
  month: Option[Long],
  day: Option[Long],
  totalCount: Long,
  impactedCount: Long,
  current: Boolean = false
)
