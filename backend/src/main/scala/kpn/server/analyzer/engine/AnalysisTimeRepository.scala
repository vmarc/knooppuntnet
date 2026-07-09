package kpn.server.analyzer.engine

import org.apache.commons.io.FileUtils

import java.io.File

/**
 * Stores the timestamp when the most recent full analysis was performed.
 */
class AnalysisTimeRepository(filename: String) {

  def get: Option[String] = {
    val file = new File(filename)
    Option.when(file.exists) {
      FileUtils.readFileToString(file, "UTF-8")
    }
  }

  // hhmm format? make sure this works OK around midnight; will need date also ?
  def put(time: String): Unit = {
    val file = new File(filename)
    FileUtils.writeStringToFile(file, time, "UTF-8")
  }
}
