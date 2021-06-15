package kpn.core.mongo.actions.base

import kpn.api.common.changes.filter.ChangesParameters

object TimeRange {

  def fromParameters(parameters: ChangesParameters): Option[TimeRange] = {
    parameters.year match {
      case None => None
      case Some(year) =>
        parameters.month match {
          case None =>
            Some(
              TimeRange(
                Seq(year.toLong, 0),
                Seq(year.toLong, 99),
              )
            )
          case Some(month) =>
            parameters.day match {
              case None =>
                Some(
                  TimeRange(
                    Seq(year.toLong, month.toLong, 0),
                    Seq(year.toLong, month.toLong, 99),
                  )
                )
              case Some(day) =>
                Some(
                  TimeRange(
                    Seq(year.toLong, month.toLong, day.toLong, 0),
                    Seq(year.toLong, month.toLong, day.toLong, 99),
                  )
                )
            }
        }
    }
  }
}

case class TimeRange(start: Seq[Long], end: Seq[Long])
