package kpn.core.util

import java.lang.management.ManagementFactory
import scala.annotation.tailrec
import scala.jdk.CollectionConverters._

/**
 * Logs memory usage every X seconds.
 */
object MemoryLog {

  private val LOG = Log(classOf[Memory])

  def log(logPeriodInSeconds: Int): Unit = {
    new MemoryLog(logPeriodInSeconds)
    ()
  }
}

class MemoryLog(logPeriodInSeconds: Int) {

  MemoryLog.LOG.info(s"Logging memory statistics every $logPeriodInSeconds seconds")

  private val startTime = System.nanoTime

  scala.sys.addShutdownHook(logMemoryUsage())
  logMemoryUsage()
  scheduleNextLog()

  private def logMemoryUsage(): Unit = {
    val elapsed: Long = (System.nanoTime - startTime) / 1000000000
    val m = currentMemoryUsage()
    val message = s"elapsed=${elapsed}s, initial=${toMb(m.init)}, used=${toMb(m.used)}, committed=${toMb(m.committed)}, max=${toMb(m.max)}"
    MemoryLog.LOG.info(message)
  }

  @tailrec
  private def scheduleNextLog(): Unit = {
    //    timer(logPeriodInSeconds) {
    MemoryLog.LOG.info("AT THIS MOMENT THE TIMER FUNCTION DOES NOT WORK ANYMORE !!!")

    logMemoryUsage()
    scheduleNextLog()
    //    }
  }

  private def currentMemoryUsage(): Memory = {
    ManagementFactory.getMemoryPoolMXBeans.asScala.
      map(_.getPeakUsage).
      map(u => Memory(u.getInit, u.getUsed, u.getCommitted, u.getMax)).
      reduceLeft(_ + _) // add up all memory pools
  }

  private def toMb(nanos: Long): String = f"${nanos / 1000000d}%.0fMb"
}

object Memory {
  def bytes: Long = {
    System.gc()
    Runtime.getRuntime.totalMemory() - Runtime.getRuntime.freeMemory()
  }
}

case class Memory(init: Long, used: Long, committed: Long, max: Long) {
  def +(x: Memory): Memory = Memory(init + x.init, used + x.used, committed + x.committed, max + x.max)
}
