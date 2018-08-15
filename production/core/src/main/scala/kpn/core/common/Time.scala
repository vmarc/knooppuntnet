package kpn.core.common

import java.time.ZoneId
import java.time.ZonedDateTime

import kpn.shared.Timestamp

/**
  * Reflects a point in time in the UTC timezone.
  *
  * Allows control over the <i>current time</i> in the
  * current execution thread (used during unit testing time dependent logic).
  *
  * If no specific time point is set, the normal system time is in effect.
  */
object Time {

  private val currentTime = new ThreadLocal[Option[Timestamp]]() {
    override def initialValue(): Option[Timestamp] = None
  }

  def now: Timestamp = {
    currentTime.get().getOrElse(system())
  }

  def set(timestamp: Timestamp): Unit = currentTime.set(Some(timestamp))

  def clear(): Unit = {
    currentTime.remove()
  }

  private def system(): Timestamp = {
    TimestampUtil.toTimestamp(ZonedDateTime.now(ZoneId.of("UTC")))
  }
}
