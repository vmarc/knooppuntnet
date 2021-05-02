package kpn.api.custom

import kpn.core.util.Util

object Day {
  def fromString(string: String): Option[Day] = {
    if (string.length == "yyyy-mm-dd".length) {
      val yearString = string.substring(0, 4)
      val monthString = string.substring(5, 7)
      val dayString = string.substring(8, 10)
      if (Util.isDigits(yearString) && Util.isDigits(monthString) && Util.isDigits(dayString)) {
        Some(Day(yearString.toInt, monthString.toInt, Some(dayString.toInt)))
      }
      else {
        None
      }
    }
    else if (string.length == "yyyy-mm".length) {
      val yearString = string.substring(0, 4)
      val monthString = string.substring(5, 7)
      if (Util.isDigits(yearString) && Util.isDigits(monthString)) {
        Some(Day(yearString.toInt, monthString.toInt, None))
      }
      else {
        None
      }
    }
    else {
      None
    }
  }
}

case class Day(year: Int, month: Int, day: Option[Int]) {

  def yyyymm: String = f"$year-$month%02d"

  def isBefore(other: Day): Boolean = {
    if (this.year < other.year) {
      true
    }
    else if (this.year > other.year) {
      false
    }
    else {
      this.month < other.month
    }
  }

  def yyyymmdd: String = {
    day match {
      case Some(dayInt) => f"$year-$month%02d-$dayInt%02d"
      case None => f"$year-$month%02d"
    }
  }
}
