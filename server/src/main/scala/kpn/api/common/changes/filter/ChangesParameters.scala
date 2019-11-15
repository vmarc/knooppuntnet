package kpn.api.common.changes.filter

import kpn.api.custom.Subset

case class ChangesParameters(

  subset: Option[Subset] = None,
  networkId: Option[Long] = None,
  routeId: Option[Long] = None,
  nodeId: Option[Long] = None,

  year: Option[String] = None,
  month: Option[String] = None,
  day: Option[String] = None,
  itemsPerPage: Long = 5,
  pageIndex: Long = 0,
  impact: Boolean = false
) {

  def toDisplayString: String = {
    Seq(
      subsetString,
      networkString,
      routeString,
      nodeString,
      yearString,
      monthString,
      dayString,
      itemsPerPageString,
      pageIndexString,
      impactString
    ).flatten.mkString
  }

  private def subsetString = subset.map(s => s"subset=${s.name}, ")

  private def networkString = networkId.map(id => s"network=$id, ")

  private def routeString = routeId.map(id => s"route=$id, ")

  private def nodeString = nodeId.map(id => s"node=$id, ")

  private def yearString = year.map(y => s"year=$y, ")

  private def monthString = month.map(m => s"month=$m, ")

  private def dayString = day.map(d => s"day=$d, ")

  private def itemsPerPageString = Some(s"itemsPerPage=$itemsPerPage, ")

  private def pageIndexString = Some(s"pageIndex=$pageIndex, ")

  private def impactString = Some(s"impact=$impact")

}
