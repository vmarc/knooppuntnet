package kpn.core.overpass

import java.io.File

import kpn.core.common.TimestampUtil
import kpn.core.util.GZipFile
import kpn.core.util.Log
import kpn.shared.Timestamp

class CachingOverpassQueryExecutor(cacheRootDir: File, val executor: OverpassQueryExecutor) extends OverpassQueryExecutor {

  private val log = Log(classOf[CachingOverpassQueryExecutor])

  override def execute(queryString: String): String = {
    executor.execute(queryString)
  }

  override def executeQuery(timestamp: Option[Timestamp], query: OverpassQuery): String = {
    timestamp match {
      case None => super.executeQuery(None, query)
      case Some(t) =>
        val cacheDir = new File(cacheRootDir, TimestampUtil.cacheDir(t))
        cacheDir.mkdirs()
        val cacheFile = new File(cacheDir, query.name + ".xml.gz")
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
