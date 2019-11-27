package kpn.server.analyzer.engine

import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.database.views.common.Design
import kpn.core.util.Log
import org.springframework.web.client.HttpServerErrorException

/**
 * Forces indexing of Couchdb views. Couchdb indexes are updated upon querying a view. We wait
 * until the view result is received as an indication that indexing is complete. We issue
 * the query again if no result is received within the timeout period.
 */
class CouchIndexer(database: Database, design: Design) {

  private val log = Log(classOf[CouchIndexer])

  def index(): Unit = {

    log.info("Start waiting for indexing to finish")

    val start = System.currentTimeMillis()
    val timeoutMinutes = 120
    val maxEnd = start + (timeoutMinutes * 60 * 1000)

    var retry = true

    while (retry) {
      try {
        val documentCounts = DocumentView.counts(database)
        val counts = documentCounts.map(dc => "    '" + dc.prefix + "'=" + dc.count).mkString("\n")
        val end = System.currentTimeMillis()
        log.info(s"Indexing completed in ${end - start}ms\n$counts")
        retry = false
      }
      catch {
        case e: HttpServerErrorException =>
          val now = System.currentTimeMillis()
          if (now > maxEnd) {
            throw new RuntimeException(s"Maximum wait time ($timeoutMinutes mins) expired")
          }
          if (e.getResponseBodyAsString.contains("The request could not be processed in a reasonable amount of time")) {
            log.info("Continue waiting for indexing to finish")
          }
          else {
            log.info("Continue waiting for indexing to finish (Exception) " + e.getMessage)
          }
      }
    }
  }
}
