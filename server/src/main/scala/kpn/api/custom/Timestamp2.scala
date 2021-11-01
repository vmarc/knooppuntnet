package kpn.api.custom

case class Timestamp2(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) {

  def yyyymmdd: String = s"$year-$monthString-$dayString"

  def hhmmss: String = s"$hourString:$minuteString:$secondString"

  def yyyymmddhhmmss: String = s"$yyyymmdd $hhmmss"

  def yyyymmddhhmm: String = yyyymmdd + s" $hourString:$minuteString"

  def yearString: String = "" + year

  def monthString: String = to2digitString(month)

  def dayString: String = to2digitString(day)

  def hourString: String = to2digitString(hour)

  def minuteString: String = to2digitString(minute)

  def secondString: String = to2digitString(second)

  def iso: String = yyyymmdd + "T" + hhmmss + "Z"

  private def to2digitString(value: Int): String = (if (value < 10) "0" else "") + value
}
