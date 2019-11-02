package kpn.server.analyzer.engine

import java.io.File

import org.apache.commons.io.FileUtils

/**
 * Stores the timestamp when the most recent full analysis was performed.
 */
trait AnalysisTimeRepository {
  def get: Option[String]

  // hhmm format? make sure this works OK arround midnight; will need date also ?
  def put(time: String): Unit
}

class AnalysisTimeRepositoryImpl(filename: String) extends AnalysisTimeRepository {

  def get: Option[String] = {
    val file = new File(filename)
    if (file.exists) {
      Some(FileUtils.readFileToString(file, "UTF-8"))
    }
    else {
      None
    }
  }

  def put(time: String): Unit = {
    val file = new File(filename)
    FileUtils.writeStringToFile(file, time, "UTF-8")
  }
}
