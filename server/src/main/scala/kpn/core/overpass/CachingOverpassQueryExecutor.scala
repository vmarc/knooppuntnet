package kpn.core.overpass

import java.io.File

import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil
import kpn.core.util.GZipFile
import kpn.core.util.Log
import org.apache.commons.io.FileUtils

class CachingOverpassQueryExecutor(cacheRootDir: File, val overpassQueryExecutor: OverpassQueryExecutor) extends OverpassQueryExecutor {

  private val log = Log(classOf[CachingOverpassQueryExecutor])

  override def execute(queryString: String): String = {
    overpassQueryExecutor.execute(queryString)
  }

  override def executeQuery(timestamp: Option[Timestamp], query: OverpassQuery): String = {
    timestamp match {
      case None => super.executeQuery(None, query)
      case Some(t) =>

        val cacheDir = new File(cacheRootDir, TimestampUtil.cacheDir(t))
        cacheDir.mkdirs()
        val cacheFile = new File(cacheDir, query.name + ".xml.gz")

        // temp code
        if (!cacheFile.exists()) {
          val oldCacheDir = new File("/kpn/cache-old/", TimestampUtil.cacheDir(t))
          val oldCacheFile = new File(oldCacheDir, query.name + ".xml.gz")
          if (oldCacheFile.exists()) {
            log.debug(s"Re-using request result ${query.name}.xml.gz")
            FileUtils.copyFile(oldCacheFile, cacheFile)
          }
          else {
            log.debug(s"Not found in old cache: ${oldCacheFile.getAbsolutePath}")
          }
        }
        // temp code until here

        if (cacheFile.exists()) {
          GZipFile.read(cacheFile.getAbsolutePath)
        }
        else {
          val response = super.executeQuery(timestamp, query)
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
  }
}
