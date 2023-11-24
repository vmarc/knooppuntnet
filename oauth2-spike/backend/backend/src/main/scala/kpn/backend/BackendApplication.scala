package kpn.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


object BackendApplication {
  def main(args: Array[String]): Unit = {
    val app: Array[Class[_]] = Array(classOf[BackendApplication])
    SpringApplication.run(app, args)
  }
}

@SpringBootApplication
class BackendApplication() {
}