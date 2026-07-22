package kpn.core.common

/**
  * Returns the current time in milliseconds.
  *
  * Allows control over the <i>current time</i> in the
  * current execution thread (used during unit testing time dependent logic).
  *
  * If no specific time point is set, the normal system time is in effect.
  */
object EpochTime {

  private val currentEpochTime = new ThreadLocal[Option[Long]]() {
    override def initialValue(): Option[Long] = None
  }

  def now: Long = {
    currentEpochTime.get().getOrElse(System.currentTimeMillis())
  }

  def set(currentTimeMillis: Long): Unit = currentEpochTime.set(Some(currentTimeMillis))

  def clear(): Unit = {
    currentEpochTime.remove()
  }
}
