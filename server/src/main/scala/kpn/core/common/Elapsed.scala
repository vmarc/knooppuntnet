package kpn.core.common

object Elapsed {

  private val secondsPerDay = 24 * 60 * 60
  private val secondsPerHour = 60 * 60
  private val secondsPerMinute = 60

  def string(elapsedMillis: Long): String = {

    val elapsedSeconds = elapsedMillis / 1000

    val days = elapsedSeconds / secondsPerDay
    val daysRemainderSeconds = elapsedSeconds % secondsPerDay

    val hours = daysRemainderSeconds / secondsPerHour
    val hoursRemainderSeconds = daysRemainderSeconds % secondsPerHour

    val minutes = hoursRemainderSeconds / secondsPerMinute
    val seconds = hoursRemainderSeconds % secondsPerMinute

    if (days > 0) {
      s"$days days, $hours hours, $minutes minutes"
    }
    else if (hours > 0) {
      s"$hours hours, $minutes minutes"
    }
    else if (minutes > 0) {
      s"$minutes minutes, $seconds seconds"
    }
    else {
      s"$seconds seconds"
    }
  }
}
