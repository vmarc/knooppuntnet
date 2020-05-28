package kpn.api.custom

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
}
