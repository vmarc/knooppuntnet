package kpn.server.repository

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset

import java.time.LocalDate

object QueryParameters {

  def locationParametersFrom(location: String, parameters: ChangesParameters): Map[String, String] = {
    val impacted = if (parameters.impact) "impacted:" else ""
    val prefix = Seq(s"${impacted}location", location)
    fromX(prefix, parameters)
  }

  def subsetParametersFrom(subset: Subset, parameters: ChangesParameters): Map[String, String] = {
    val key = subset.key
    val impacted = if (parameters.impact) "impacted:" else ""
    val prefix = Seq(s"$key:${impacted}change-set")
    fromX(prefix, parameters)
  }

  def networkParametersFrom(networkId: Long, parameters: ChangesParameters): Map[String, String] = {
    val impacted = if (parameters.impact) "impacted:" else ""
    val prefix = Seq(s"${impacted}network", s"$networkId")
    fromX(prefix, parameters)
  }

  def routeParametersFrom(routeId: Long, parameters: ChangesParameters): Map[String, String] = {
    val impacted = if (parameters.impact) "impacted:" else ""
    val prefix = Seq(s"${impacted}route", s"$routeId")
    fromX(prefix, parameters)
  }

  def nodeParametersFrom(nodeId: Long, parameters: ChangesParameters): Map[String, String] = {
    val impacted = if (parameters.impact) "impacted:" else ""
    val prefix = Seq(s"${impacted}node", s"$nodeId")
    fromX(prefix, parameters)
  }

  def parametersFrom(parameters: ChangesParameters): Map[String, String] = {
    val impacted = if (parameters.impact) "impacted:" else ""
    val prefix = Seq(s"${impacted}change-set")
    fromX(prefix, parameters)
  }

  private def fromX(prefix: Seq[String], parameters: ChangesParameters): Map[String, String] = {

    val startKeys = parameters.year match {
      case None => Seq("9999")
      case Some(year) =>
        val startDate = LocalDate.of(
          year.toInt,
          parameters.month.map(_.toInt).getOrElse(1),
          parameters.day.map(_.toInt).getOrElse(1)
        )

        parameters.month match {
          case None =>

            val nextDate = startDate.plusYears(1)
            val yyyy = nextDate.getYear.toString
            Seq(yyyy)

          case Some(month) =>

            parameters.day match {
              case None =>

                val nextDate = startDate.plusMonths(1)
                val yyyy = nextDate.getYear.toString
                val mm = (100 + nextDate.getMonthValue).toString.substring(1)
                Seq(yyyy, mm)

              case Some(day) =>

                val nextDate = startDate.plusDays(1)
                val yyyy = nextDate.getYear.toString
                val mm = (100 + nextDate.getMonthValue).toString.substring(1)
                val dd = (100 + nextDate.getDayOfMonth).toString.substring(1)
                Seq(yyyy, mm, dd)
            }
        }
    }

    val endKeys = Seq(parameters.year, parameters.month, parameters.day).flatten

    val startKey = keyString(prefix ++ startKeys)
    val endKey = keyString(prefix ++ endKeys)

    val skip: String = (parameters.itemsPerPage * parameters.pageIndex).toString
    val limit: String = parameters.itemsPerPage.toString

    Map(
      "startkey" -> startKey,
      "endkey" -> endKey,
      "include_docs" -> "true",
      "reduce" -> "false",
      "descending" -> "true",
      "skip" -> skip,
      "limit" -> limit
    )
  }

  private def keyString(values: Seq[String]): String = values.mkString("[\"", "\", \"", "\"]")
}
