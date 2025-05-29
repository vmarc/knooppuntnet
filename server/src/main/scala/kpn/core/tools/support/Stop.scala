package kpn.core.tools.support

import kpn.core.replicate.OperMBean
import kpn.core.util.Log
import kpn.database.base.Exit
import org.springframework.boot.admin.SpringApplicationAdminMXBean

import javax.management.JMX
import javax.management.ObjectName
import javax.management.remote.JMXConnectorFactory
import javax.management.remote.JMXServiceURL

case class StopOptions(port: String = "")

object StopOptions {

  def parse(args: Array[String]): Option[StopOptions] = {
    optionParser.parse(args, StopOptions())
  }

  private def optionParser: scopt.OptionParser[StopOptions] = {
    new scopt.OptionParser[StopOptions]("stop") {
      opt[String]("port").required() valueName "port" action { (x, c) =>
        c.copy(port = x)
      } text "jmx rmi port"
    }
  }
}

object Stop {
  private val log = Log(classOf[Stop])

  def main(args: Array[String]): Unit = {
    val exitCode = execute(args)
    System.exit(exitCode)
  }

  private def execute(args: Array[String]): Int = {
    try {
      StopOptions.parse(args) match {
        case Some(options) => executeWithOptions(options)
        case None =>
          // arguments are bad, error message will have been displayed
          Exit.Failure
      }
    } catch {
      case e: Exception =>
        log.error(e.getMessage)
        Exit.Failure
    }
  }

  private def executeWithOptions(options: StopOptions): Int = {
    new Stop().stop(options.port)
    Exit.Success
  }
}

class Stop {
  def stop(port: String): Unit = {
    val url = new JMXServiceURL(s"service:jmx:rmi:///jndi/rmi://:$port/jmxrmi")
    val connector = JMXConnectorFactory.connect(url)
    try {
      val connection = connector.getMBeanServerConnection
      val mbeanName: ObjectName = new ObjectName("kpn:type=Oper")
      val mbeanProxy: OperMBean = JMX.newMBeanProxy(connection, mbeanName, classOf[OperMBean], true)
      mbeanProxy.stop()
    }
    finally {
      connector.close()
    }
  }

  def stopServer(port: String): Unit = {
    val url = new JMXServiceURL(s"service:jmx:rmi:///jndi/rmi://:$port/jmxrmi")
    val connector = JMXConnectorFactory.connect(url)
    try {
      val connection = connector.getMBeanServerConnection
      val mbeanName: ObjectName = new ObjectName("org.springframework.boot:type=Admin,name=SpringApplication")
      val mbeanProxy: SpringApplicationAdminMXBean = JMX.newMBeanProxy(connection, mbeanName, classOf[SpringApplicationAdminMXBean], true)
      mbeanProxy.shutdown()
    }
    finally {
      connector.close()
    }
  }
}
