package kpn.core.replicate

import java.lang.management.ManagementFactory
import java.util.concurrent.atomic.AtomicBoolean

import javax.management.ObjectName
import kpn.core.util.Log

class Oper() extends OperMBean {

  private val LOG = Log(classOf[Oper])

  private val shutdown = new AtomicBoolean(false)

  buildMBeanServer()

  def stop(): Unit = {
    println("Received signal to shutdown")
    LOG.info("Received signal to shutdown")
    shutdown.set(true)
  }

  def isActive: Boolean = {
    !shutdown.get()
  }

  private def buildMBeanServer(): Unit = {
    val mbeanServer = ManagementFactory.getPlatformMBeanServer
    mbeanServer.registerMBean(this, new ObjectName("kpn:type=Oper"))
    ()
  }
}
