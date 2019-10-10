package kpn.core.util

import java.lang.management.ManagementFactory

import scala.annotation.tailrec
import scala.collection.JavaConverters.asScalaBufferConverter
//import scala.tools.nsc.io.timer

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

  MemoryLog.LOG.info("Logging memory statistics every " + logPeriodInSeconds + " seconds")

  private val startTime = System.nanoTime

  scala.sys.addShutdownHook(logMemoryUsage())
  logMemoryUsage()
  scheduleNextLog()

  private def logMemoryUsage(): Unit = {
    val elapsed: Long = (System.nanoTime - startTime) / 1000000000
    val m = currentMemoryUsage()
    val message = "elapsed=%ds, initial=%s, used=%s, committed=%s, max=%s".format(
      elapsed,
      toMb(m.init),
      toMb(m.used),
      toMb(m.committed),
      toMb(m.max)
    )
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
      reduceLeft (_ + _) // add up all memory pools
  }

  private def toMb(nanos: Long): String = "%.0fMb".format(nanos / 1000000d)
}

case class Memory(init: Long, used: Long, committed: Long, max: Long) {
  def +(x: Memory): Memory = Memory(init + x.init, used + x.used, committed + x.committed, max + x.max)
}
