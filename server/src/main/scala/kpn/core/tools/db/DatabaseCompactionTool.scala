package kpn.core.tools.db

import kpn.core.database.implementation.DatabaseContextImpl
import kpn.core.db.couch.Couch
import kpn.core.util.Log
import kpn.server.json.Json
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

    val host = "kpn-frontend"
    val port = Couch.config.port

    val backendCompactions = Seq(
      DatabaseCompaction("analysis", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("analysis1", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("attic-analysis", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("attic-changes", Seq("ChangeDocumentsDesign", "ChangesDesign")),
      DatabaseCompaction("backend-actions", Seq("BackendMetricsDesign")),
      DatabaseCompaction("backend-actions-new", Seq()),
      DatabaseCompaction("changes", Seq("ChangeDocumentsDesign", "ChangesDesign")),
      DatabaseCompaction("changes-test", Seq("ChangeDocumentsDesign", "ChangesDesign")),
      DatabaseCompaction("changes-tmp", Seq("ChangeDocumentsDesign", "ChangesDesign")),
      // DatabaseCompaction("changes1", Seq("ChangeDocumentsDesign", "ChangesDesign")), // 36Gb !!
      DatabaseCompaction("changesets2", Seq()),
      DatabaseCompaction("frontend-actions", Seq("FrontEndActions")),
      DatabaseCompaction("master-test", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("master-test-1", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("master-test-2", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("master1", Seq("AnalyzerDesign", "LocationDesign")),
      DatabaseCompaction("master2", Seq("AnalyzerDesign", "LocationDesign")),
      DatabaseCompaction("pois3", Seq("PoiDesign")),
      DatabaseCompaction("pois4", Seq("PoiDesign")),
      DatabaseCompaction("pois5", Seq("PoiDesign")),
      DatabaseCompaction("tasks-test", Seq()),
      DatabaseCompaction("tasks1", Seq())
    )

    val frontendCompactions = Seq(
      DatabaseCompaction("analysis", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("analysis1", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("backend-actions", Seq("BackendMetricsDesign")),
      DatabaseCompaction("changes", Seq("ChangeDocumentsDesign", "ChangesDesign")),
      DatabaseCompaction("changes-test", Seq("ChangeDocumentsDesign", "ChangesDesign")),
      // DatabaseCompaction("changes1", Seq("ChangeDocumentsDesign", "ChangesDesign")), // 36Gb !!
      DatabaseCompaction("changesets2", Seq()),
      DatabaseCompaction("frontend-actions", Seq("FrontEndActions")),
      DatabaseCompaction("master-test", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("master-test-1", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("master-test-2", Seq("AnalyzerDesign", "LocationDesign", "PlannerDesign", "TileDesign")),
      DatabaseCompaction("master1", Seq("AnalyzerDesign", "LocationDesign")),
      DatabaseCompaction("master2", Seq("AnalyzerDesign", "LocationDesign")),
      DatabaseCompaction("pois2", Seq("PoiDesign")),
      DatabaseCompaction("pois4", Seq("PoiDesign")),
    )


    val context = DatabaseContextImpl(Couch.config, Json.objectMapper, "")
    val template = context.authenticatedRestTemplate

    try {
      new DatabaseCompactionTool(template, s"http://$host:$port").go(frontendCompactions)
    }
    catch {
      case e: Exception =>
        log.error("Abort", e)
    }
    log.info("Done")
  }
}

class DatabaseCompactionTool(template: RestOperations, baseUrl: String) {

  import DatabaseCompactionTool.log

  def go(compactions: Seq[DatabaseCompaction]): Unit = {
    waitUntilNoCompactionActive(immediate = true)
    compactions.foreach { compaction =>
      compact(compaction.name)
      waitUntilNoCompactionActive()
      compaction.designs.foreach { design =>
        compact(compaction.name, design)
        waitUntilNoCompactionActive()
      }
      viewCleanup(compaction.name)
    }
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
