package kpn.api.common.changes.filter

case class ChangesParameters(
  year: Option[Long] = None,
  month: Option[Long] = None,
  day: Option[Long] = None,
  pageSize: Long = 5,
  pageIndex: Long = 0,
  impact: Boolean = false,
) {

  def toDisplayString: String = {
    Seq(
      yearString,
      monthString,
      dayString,
      pageSizeString,
      pageIndexString,
      impactString
    ).flatten.mkString
  }

  private def yearString = year.map(y => s"year=${y.toString}, ")

  private def monthString = month.map(m => s"month=${m.toString}, ")

  private def dayString = day.map(d => s"day=${d.toString}, ")

  private def pageSizeString = Some(s"pageSize=$pageSize, ")

  private def pageIndexString = Some(s"pageIndex=$pageIndex, ")

  private def impactString = Some(s"impact=$impact")

}
