package kpn.server.api.status

import javax.annotation.PostConstruct
import kpn.api.common.status.ActionTimestamp
import kpn.core.action.SystemStatus
import kpn.core.action.SystemStatusValue
import kpn.core.util.Log
import kpn.server.json.Json
import kpn.server.repository.BackendMetricsRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

import scala.sys.process.Process

case class DatabaseSampleConfig(
  name: String,
  host: String,
  database: String
)

@Component
class SystemStatusMonitorImpl(
  systemMetricsEnabled: Boolean,
  backendActionsRepository: BackendMetricsRepository
) extends SystemStatusMonitor {

  private val log = Log(classOf[SystemStatusMonitorImpl])

  @PostConstruct
  def postConstruct(): Unit = {
    if (systemMetricsEnabled) {
      log.info("system metrics not enabled")
    }
  }

  @Scheduled(cron = "0 0/5 * * * *")
  def snapshot(): Unit = {
    if (systemMetricsEnabled) {
      backendActionsRepository.saveSystemStatus(
        SystemStatus(
          ActionTimestamp.now(),
          allSystemStatusValues()
        )
      )
      log.info("system metrics saved")
    }
  }

  private def allSystemStatusValues(): Seq[SystemStatusValue] = {
    Seq(
      systemStatusValues("/kpn/scripts/status-backend.sh"),
      systemStatusValues("rsh kpn-frontend /kpn/scripts/status-frontend.sh"),
      systemStatusValues("rsh kpn-database /kpn/scripts/status-database.sh"),
      databaseInfo()
    ).flatten
  }

  private def systemStatusValues(command: String): Seq[SystemStatusValue] = {
    try {
      val contents = Process(command).lazyLines
      contents.filterNot(_.startsWith("#")).map { line =>
        val splitted = line.split("=")
        SystemStatusValue(splitted.head, splitted(1).toLong)
      }
    }
    catch {
      case e: RuntimeException =>
        log.error("Could not execute command: '$command':" + e.getMessage)
        Seq()
    }
  }

  private def databaseInfo(): Seq[SystemStatusValue] = {

    databaseSampleConfig().flatMap { config =>

      val url = s"http://${config.host}:5984/${config.database}"
      val restTemplate = new RestTemplate
      val headers = new HttpHeaders()
      val entity = new HttpEntity[String]("", headers)

      try {
        val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.GET, entity, classOf[String])
        response.getStatusCode match {
          case HttpStatus.OK =>

            val databaseInfo = Json.objectMapper.readValue(response.getBody, classOf[DatabaseInfo])
            Seq(
              SystemStatusValue(s"${config.name}-docs", databaseInfo.doc_count),
              SystemStatusValue(s"${config.name}-disk-size", databaseInfo.sizes.file),
              SystemStatusValue(s"${config.name}-data-size-external", databaseInfo.sizes.external),
              SystemStatusValue(s"${config.name}-data-size", databaseInfo.sizes.active)
            )
          case _ =>
            // TODO log error
            println("ERROR " + url)
            Seq()
        }
      }
      catch {
        case e: HttpClientErrorException.NotFound =>
          // TODO log error
          println("NOT FOUND" + url)
          Seq()
      }
    }
  }

  private def databaseSampleConfig(): Seq[DatabaseSampleConfig] = {
    Seq(
      DatabaseSampleConfig("backend-analysis", "kpn-database", "master-test-1"),
      DatabaseSampleConfig("backend-changes", "kpn-database", "changes-test"),
      DatabaseSampleConfig("backend-changesets", "kpn-database", "changesets2"),
      DatabaseSampleConfig("backend-tasks", "kpn-database", "tasks-test"),
      DatabaseSampleConfig("backend-backend-actions", "kpn-database", "backend-actions"),
      DatabaseSampleConfig("backend-frontend-actions", "kpn-database", "frontend-actions"),

      DatabaseSampleConfig("old-backend-analysis", "kpn-database", "master2"),
      DatabaseSampleConfig("old-backend-changes", "kpn-database", "changes1"),
      DatabaseSampleConfig("old-backend-changesets", "kpn-database", "changesets2"),
      DatabaseSampleConfig("old-backend-tasks", "kpn-database", "tasks-test"),

      DatabaseSampleConfig("frontend-analysis", "kpn-frontend", "master-test-1"),
      DatabaseSampleConfig("frontend-changes", "kpn-frontend", "changes-test"),
      DatabaseSampleConfig("frontend-changesets", "kpn-frontend", "changesets2"),
      DatabaseSampleConfig("frontend-backend-actions", "kpn-frontend", "backend-actions"),
      DatabaseSampleConfig("frontend-frontend-actions", "kpn-frontend", "frontend-actions"),

      DatabaseSampleConfig("old-frontend-analysis", "kpn-frontend", "master2"),
      DatabaseSampleConfig("old-frontend-changes", "kpn-frontend", "changes1"),
      DatabaseSampleConfig("old-frontend-changesets", "kpn-frontend", "changesets2"),
    )
  }

}
