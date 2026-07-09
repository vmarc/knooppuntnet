package kpn.core.util

object Elapsed {

  private val minutesPerHour = 60
  private val secondsPerMinute = 60
  private val millisPerSecond = 1000
  private val millisPerMinute = secondsPerMinute * millisPerSecond
  private val millisPerHour = minutesPerHour * secondsPerMinute * millisPerSecond

  def apply(millis: Long): String = {
    val hours = millis / millisPerHour
    val hoursRemainder = millis - (hours * millisPerHour)

    val minutes = hoursRemainder / millisPerMinute
    val minutesRemainder = hoursRemainder - (minutes * millisPerMinute)

    val seconds = minutesRemainder / millisPerSecond
    val milliseconds = minutesRemainder - (seconds * millisPerSecond)

    if (hours > 0) {
      f"$hours:$minutes%02d:$seconds%02d"
    }
    else if (minutes > 0) {
      f"$minutes:$seconds%02d"
    }
    else if (seconds > 10) {
      s"${seconds}s"
    }
    else if (seconds > 0) {
      f"$seconds.$milliseconds%03ds"
    }
    else {
      f"0.$milliseconds%03ds"
    }
  }
}
