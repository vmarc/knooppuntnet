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
      "%dhrs, %dmins, %ds".format(hours, minutes, seconds)
    }
    else if (minutes > 0) {
      "%dmins, %ds".format(minutes, seconds)
    }
    else if (seconds > 10) {
      "%ds".format(seconds)
    }
    else if (seconds > 0) {
      "%ds, %dms".format(seconds, milliseconds)
    }
    else {
      "%dms".format(milliseconds)
    }
  }
}
