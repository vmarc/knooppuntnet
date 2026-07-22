package kpn.core.common

import java.lang.management.ManagementFactory

/**
  * Represents the total CPU time used by the current thread in milliseconds.
  *
  * Allows control over the <i>cpu time</i> in the
  * current execution thread (used during unit testing time dependent logic).
  *
  * If no specific cpu time is set, the normal cpu time is in effect.
  */
object CpuTime {

  private val currentCpuTime = new ThreadLocal[Option[Long]]() {
    override def initialValue(): Option[Long] = None
  }

  def now: Long = {
    currentCpuTime.get().getOrElse(currentCpuTimeMillis())
  }

  def set(cpuTimeMillis: Long): Unit = currentCpuTime.set(Some(cpuTimeMillis))

  def clear(): Unit = {
    currentCpuTime.remove()
  }

  private def currentCpuTimeMillis(): Long = {
    val cpuNanoSeconds = ManagementFactory.getThreadMXBean.getCurrentThreadCpuTime
    if (cpuNanoSeconds == -1) {
      throw new UnsupportedOperationException("Thread cpu time not enabled")
    }
    cpuNanoSeconds / 1000000L
  }
}
