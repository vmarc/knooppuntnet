package kpn.core.mongo.changes

import kpn.api.common.changes.filter.ChangesParameters

object Range {
  def fromParameters(parameters: ChangesParameters): Option[Range] = {
    parameters.year match {
      case None => None
      case Some(year) =>
        parameters.month match {
          case None =>
            Some(
              Range(
                s"$year-00-00T00:00:00Z",
                s"$year-99-99T99:99:99Z"
              )
            )
          case Some(month) =>
            parameters.day match {
              case None =>
                Some(
                  Range(
                    s"$year-$month-00T00:00:00Z",
                    s"$year-$month-99T99:99:99Z"
                  )
                )
              case day =>
                Some(
                  Range(
                    s"$year-$month-${day}T00:00:00Z",
                    s"$year-$month-${day}T99:99:99Z"
                  )
                )
            }
        }
    }
  }
}

case class Range(start: String, end: String)
