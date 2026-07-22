package kpn.core.overpass

import kpn.api.custom.Timestamp
import kpn.api.time.TimestampUtil
import kpn.core.util.GZipFile
import kpn.core.util.Log

import java.io.File

/**
  * Wrapper around another OverpassQueryExecutor adding file-based caching for timestamped Overpass queries.
  *
  * - No caching if the query has no timestamp (current situation).
  * - If a query has a timestamp, the result is cached on disk as a gzipped XML file.
  * - On later executions of the same query for the same timestamp, the cached result is reused instead of calling Overpass again.
  * - Cache files are stored under a directory structure based on the timestamp.
  */
class CachingOverpassQueryExecutor(cacheRootDir: File, val overpassQueryExecutor: OverpassQueryExecutor) extends OverpassQueryExecutor {

  private val log = Log(classOf[CachingOverpassQueryExecutor])

  override def execute(queryString: String): String = {
    // no caching (caching requires timestamp and query name)
    overpassQueryExecutor.execute(queryString)
  }

  override def executeQuery(timestamp: Option[Timestamp], query: OverpassQuery): String = {
    timestamp match {
      case None => super.executeQuery(None, query)
      case Some(t) => executeTimestampedQuery(t, query)
    }
  }

  private def executeTimestampedQuery(timestamp: Timestamp, query: OverpassQuery): String = {
    val cacheFile = cacheFileFor(timestamp, query)
    if (cacheFile.exists()) {
      cachedResult(cacheFile)
    }
    else {
      executeQueryAndCache(timestamp, query, cacheFile)
    }
  }

  private def cacheFileFor(timestamp: Timestamp, query: OverpassQuery): File = {
    val cacheDir = new File(cacheRootDir, TimestampUtil.cacheDir(timestamp))
    cacheDir.mkdirs()
    new File(cacheDir, s"${query.name}.xml.gz")
  }

  private def cachedResult(cacheFile: File) = {
    GZipFile.read(cacheFile.getAbsolutePath)
  }

  private def executeQueryAndCache(timestamp: Timestamp, query: OverpassQuery, cacheFile: File): String = {
    val response = super.executeQuery(Some(timestamp), query)
    /*
       We write the response to a temporary file first, and then move the complete temporary
       file to the final destination in cache directory, to make sure that another thread
       will not try to read a file from cache that is not completely written yet.
       We no longer use the default temporary-file directory as specified by the 'java.io.tmpdir'
       system property, because that caused some problems for the 'File.renameTo' to move
       the file at some point.
    */
    val dir = new File(cacheRootDir, "tmp")
    dir.mkdirs()
    val file = File.createTempFile("overpass-query-", ".xml.gz", dir)
    GZipFile.write(file.getAbsolutePath, response)
    if (!file.renameTo(cacheFile)) {
      log.warn(s"Failed to move ${file.getAbsolutePath} to ${cacheFile.getAbsolutePath}, continue processing without caching the result")
    }
    response
  }
}
