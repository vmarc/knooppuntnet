package kpn.core.tools.db

import java.io.File

import kpn.core.database.implementation.DatabaseContextImpl
import kpn.core.db.couch.Couch
import kpn.core.util.Log
import kpn.server.json.Json
import org.apache.commons.io.FileUtils
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestOperations

object DatabaseCompactionTool {

  private val log = Log(classOf[DatabaseCompactionTool])

  def main(args: Array[String]): Unit = {

    val exit = DatabaseCompactionToolOptions.parse(args) match {
      case Some(options) =>

        log.info("Start")

        val context = DatabaseContextImpl(Couch.config, Json.objectMapper, "")
        val template = context.authenticatedRestTemplate

        try {
          val url = s"http://${options.host}:${Couch.config.port}"
          new DatabaseCompactionTool(template, url).go(options.compactions)
          log.info(s"Done")
        }
        catch {
          case e: Exception =>
            log.error("Abort", e)
        }
        finally {
          ()
        }

        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }
}

class DatabaseCompactionTool(template: RestOperations, baseUrl: String) {

  import DatabaseCompactionTool.log

  def go(configurationFilename: String): Unit = {
    val compactions = readCompactions(configurationFilename)
    waitUntilNoCompactionActive(immediate = true)
    compactions.foreach { compaction =>
      compact(compaction.database)
      waitUntilNoCompactionActive()
      compaction.designs.foreach { design =>
        compact(compaction.database, design)
        waitUntilNoCompactionActive()
      }
      viewCleanup(compaction.database)
    }
  }

  private def readCompactions(configurationFilename: String): Seq[DatabaseCompaction] = {
    val configuration = FileUtils.readFileToString(new File(configurationFilename), "UTF-8")
    Json.objectMapper.readValue(configuration, classOf[DatabaseCompactions]).compactions
  }

  private def compact(database: String): Unit = {
    log.info("compact " + database)
    post(s"$baseUrl/$database/_compact")
  }

  private def compact(database: String, design: String): Unit = {
    log.info(s"compact $database/$design")
    post(s"$baseUrl/$database/_compact/$design")
  }

  private def viewCleanup(database: String): Unit = {
    log.info("viewCleanup " + database)
    post(s"$baseUrl/$database/_view_cleanup")
  }

  private def waitUntilNoCompactionActive(immediate: Boolean = false): Unit = {
    if (!immediate) {
      Thread.sleep(10000)
    }
    while (compactionActive()) {
      Thread.sleep(10000)
    }
  }

  private def compactionActive(): Boolean = {
    activeTasks().exists(task => task.`type`.endsWith("compaction"))
  }

  private def activeTasks(): Seq[CouchTask] = {
    val response = template.getForEntity(s"$baseUrl/_active_tasks", classOf[String])
    val string = s"""{"tasks": ${response.getBody}}"""
    Json.objectMapper.readValue(string, classOf[CouchTasks]).tasks
  }

  private def post(url: String): Unit = {
    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE)
    val entity = new HttpEntity[String]("", headers)
    val response: ResponseEntity[String] = template.exchange(url, HttpMethod.POST, entity, classOf[String])
    if (response.getStatusCode != HttpStatus.ACCEPTED) {
      throw new IllegalStateException(s"POST failed: ${response.getBody}")
    }
  }

}
