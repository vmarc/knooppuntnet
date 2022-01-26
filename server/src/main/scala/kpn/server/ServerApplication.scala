package kpn.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.autoconfigure.metrics.mongo.MongoMetricsAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.scheduling.annotation.EnableScheduling

object ServerApplication {
  def main(args: Array[String]): Unit = {
    val app: Array[Class[_]] = Array(classOf[ServerApplication])
    SpringApplication.run(app, args)
  }
}

@SpringBootApplication(exclude =
  Array(
    classOf[MongoReactiveAutoConfiguration],
    classOf[MongoMetricsAutoConfiguration],
  )
)
@EnableScheduling
class ServerApplication() {
}
