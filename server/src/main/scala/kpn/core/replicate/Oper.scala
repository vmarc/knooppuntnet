package kpn.core.replicate

import java.lang.management.ManagementFactory
import java.util.concurrent.atomic.AtomicBoolean

import javax.management.ObjectName
import kpn.core.util.Log

object Oper {
  private val SLEEP_SHUTDOWN_POLL_INTERVAL = 250L
}

class Oper() extends OperMBean {

  private val log = Log(classOf[Oper])

  private val shutdown = new AtomicBoolean(false)

  buildMBeanServer()

  def stop(): Unit = {
    println("Received signal to shutdown")
    log.info("Received signal to shutdown")
    shutdown.set(true)
  }

  def isActive: Boolean = {
    !shutdown.get()
  }

  def sleep(seconds: Int): Unit = {
    log.debug(s"Waiting ${seconds}s")
    val end = System.currentTimeMillis() + (seconds * 1000)
    while (isActive && System.currentTimeMillis() < end) {
      Thread.sleep(Oper.SLEEP_SHUTDOWN_POLL_INTERVAL)
    }
    log.debug(s"End waiting ${seconds}s")
  }

  private def buildMBeanServer(): Unit = {
    val mbeanServer = ManagementFactory.getPlatformMBeanServer
    mbeanServer.registerMBean(this, new ObjectName("kpn:type=Oper"))
    ()
  }

}
