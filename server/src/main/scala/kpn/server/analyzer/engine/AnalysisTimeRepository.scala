package kpn.server.analyzer.engine

import org.apache.commons.io.FileUtils

import java.io.File

/**
 * Stores the timestamp when the most recent full analysis was performed.
 */
trait AnalysisTimeRepository {
  def get: Option[String]

  // hhmm format? make sure this works OK around midnight; will need date also ?
  def put(time: String): Unit
}

class AnalysisTimeRepositoryImpl(filename: String) extends AnalysisTimeRepository {

  def get: Option[String] = {
    val file = new File(filename)
    Option.when(file.exists) {
      FileUtils.readFileToString(file, "UTF-8")
    }
  }

  def put(time: String): Unit = {
    val file = new File(filename)
    FileUtils.writeStringToFile(file, time, "UTF-8")
  }
}
