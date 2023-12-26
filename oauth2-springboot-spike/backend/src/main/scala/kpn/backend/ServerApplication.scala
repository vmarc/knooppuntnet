package kpn.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object ServerApplication {
  def main(args: Array[String]): Unit = {
    val app: Array[Class[_]] = Array(classOf[ServerApplication])
    SpringApplication.run(app, args)
  }
}

@SpringBootApplication()
class ServerApplication() {
}
