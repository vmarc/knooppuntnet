package kpn.monitor

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

object AppMonitorApplication {
  def main(args: Array[String]): Unit = {
    val app: Array[Class[_]] = Array(classOf[AppMonitorApplication])
    SpringApplication.run(app, args)
  }
}

@SpringBootApplication
@EnableScheduling
class AppMonitorApplication() {
}
