package kpn.core.tools.support

import kpn.core.replicate.OperMBean
import kpn.core.util.Log
import kpn.database.base.Options
import kpn.database.base.Tool
import org.springframework.boot.admin.SpringApplicationAdminMXBean

import javax.management.JMX
import javax.management.ObjectName
import javax.management.remote.JMXConnectorFactory
import javax.management.remote.JMXServiceURL

case class StopOptions(port: String = "")

object StopOptions extends Options[StopOptions] {

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

object Stop extends Tool[StopOptions] {
  private val log = Log(classOf[Stop])

  override def options: Options[StopOptions] = StopOptions

  override def execute(options: StopOptions): Unit = {
    new Stop().stop(options.port)
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
