package kpn.api.custom

object Timestamp {

  val redaction: Timestamp = Timestamp(2012, 9, 12, 6, 55, 0)
  val analysisStart: Timestamp = Timestamp(2019, 11, 1, 0, 0, 0) // 003/739/602

  implicit def timestampOrdering: Ordering[Timestamp] = (x: Timestamp, y: Timestamp) => {
    x.compareTo(y)
  }

  def fromKey(key: String): Timestamp = {
    require(key.length == "yyyyMMddHHmmss".length)
    val year = key.substring(0, 4).toInt
    val month = key.substring(4, 6).toInt
    val day = key.substring(6, 8).toInt
    val hour = key.substring(8, 10).toInt
    val minute = key.substring(10, 12).toInt
    val second = key.substring(12, 14).toInt
    Timestamp(year, month, day, hour, minute, second)
  }

  def fromLogKey(key: String): Timestamp = {
    require(key.length == "yyyy-MM-dd HH:mm".length)
    val year = key.substring(0, 4).toInt
    val month = key.substring(5, 7).toInt
    val day = key.substring(8, 10).toInt
    val hour = key.substring(11, 13).toInt
    val minute = key.substring(14, 16).toInt
    Timestamp(year, month, day, hour, minute, 0)
  }

  def fromIso(iso: String): Timestamp = {
    val year = iso.substring(0, 4).toInt
    val month = iso.substring(5, 7).toInt
    val day = iso.substring(8, 10).toInt
    val hour = iso.substring(11, 13).toInt
    val minute = iso.substring(14, 16).toInt
    val second = iso.substring(17, 19).toInt
    Timestamp(year, month, day, hour, minute, second)
  }

  def apply(day: Day): Timestamp = {
    val dayOfMonth = day.day.getOrElse(1)
    this (day.year, day.month, dayOfMonth, 0, 0, 0)
  }

  def apply(year: Int, month: Int, day: Int): Timestamp = {
    Timestamp(year, month, day, 0, 0, 0)
  }
}

case class Timestamp(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) {

  def key: String = s"$year$monthString$dayString$hourString$minuteString$secondString"

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

  def >(other: Timestamp): Boolean = {
    if (year > other.year) {
      true
    }
    else if (year < other.year) {
      false
    }
    else if (month > other.month) {
      true
    }
    else if (month < other.month) {
      false
    }
    else if (day > other.day) {
      true
    }
    else if (day < other.day) {
      false
    }
    else if (hour > other.hour) {
      true
    }
    else if (hour < other.hour) {
      false
    }
    else if (minute > other.minute) {
      true
    }
    else if (minute < other.minute) {
      false
    }
    else {
      second > other.second
    }
  }

  def <(other: Timestamp): Boolean = {
    if (year < other.year) {
      true
    }
    else if (year > other.year) {
      false
    }
    else if (month < other.month) {
      true
    }
    else if (month > other.month) {
      false
    }
    else if (day < other.day) {
      true
    }
    else if (day > other.day) {
      false
    }
    else if (hour < other.hour) {
      true
    }
    else if (hour > other.hour) {
      false
    }
    else if (minute < other.minute) {
      true
    }
    else if (minute > other.minute) {
      false
    }
    else {
      second < other.second
    }
  }

  def ===(other: Timestamp): Boolean = {
    year == other.year &&
      month == other.month &&
      day == other.day &&
      hour == other.hour &&
      minute == other.minute &&
      second == other.second
  }

  def >=(other: Timestamp): Boolean = {
    this === other || this > other
  }

  def compareTo(other: Timestamp): Int = {
    if (this > other) {
      1
    }
    else if (this < other) {
      -1
    }
    else {
      0
    }
  }

  private def to2digitString(value: Int): String = (if (value < 10) "0" else "") + value
}
