package kpn.api.common.changes.filter

case class ChangesParameters(
  year: Option[String] = None,
  month: Option[String] = None,
  day: Option[String] = None,
  itemsPerPage: Long = 5,
  pageIndex: Long = 0,
  impact: Boolean = false
) {

  def toDisplayString: String = {
    Seq(
      yearString,
      monthString,
      dayString,
      itemsPerPageString,
      pageIndexString,
      impactString
    ).flatten.mkString
  }

  private def yearString = year.map(y => s"year=$y, ")

  private def monthString = month.map(m => s"month=$m, ")

  private def dayString = day.map(d => s"day=$d, ")

  private def itemsPerPageString = Some(s"itemsPerPage=$itemsPerPage, ")

  private def pageIndexString = Some(s"pageIndex=$pageIndex, ")

  private def impactString = Some(s"impact=$impact")

}
