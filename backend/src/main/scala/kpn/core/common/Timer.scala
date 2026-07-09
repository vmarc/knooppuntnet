package kpn.core.common

/*
  CPU time base timer that is started at construction, and can be polled for timeout.

  Use this to avoid stop-the-world garbage collection to have an impact as would be the
  case in a epoch time base equivalent.

  Retrieving the CPU time used by the current thread is a fairly expensive operation, so
  we only do after the elapsed time has exceeded the timeout period, and even then only
  every X milliseconds.
*/
class Timer(durationMillis: Long) {

  private val cpuTimeStart = CpuTime.now
  private val elapsedTimeStart = EpochTime.now
  private val minTimeBetweenCpuTimeRetrieves = 50L
  private var cpuTimeRetrieved: Option[Long] = None

  def poll(): TimerState = {

    val now = EpochTime.now
    val currentEpochTimeElapsed = now - elapsedTimeStart

    if (isExceedingTimeoutDuration(currentEpochTimeElapsed)) {

      if (shouldRetrieveCpuTime(now)) {
        cpuTimeRetrieved = Some(now)
        val currentCpuTimeElapsed = CpuTime.now - cpuTimeStart
        TimerState(
          epochTimeElapsed = true,
          cpuTimeElapsed = isExceedingTimeoutDuration(currentCpuTimeElapsed),
          currentEpochTimeElapsed,
          Some(currentCpuTimeElapsed)
        )
      }
      else {
        TimerState(epochTimeElapsed = true, cpuTimeElapsed = false, currentEpochTimeElapsed, None)
      }
    }
    else {
      TimerState(epochTimeElapsed = false, cpuTimeElapsed = false, currentEpochTimeElapsed, None)
    }
  }

  private def shouldRetrieveCpuTime(now: Long): Boolean = {
    cpuTimeRetrieved match {
      case Some(time) => now - time - minTimeBetweenCpuTimeRetrieves > 0
      case None => true
    }
  }

  private def isExceedingTimeoutDuration(elapsedMillis: Long): Boolean = {
    elapsedMillis - durationMillis > 0
  }
}
