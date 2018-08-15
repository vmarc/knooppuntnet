package kpn.core.tools.analyzer

import scalax.file.Path

/**
 * Stores the timestamp when the most recent full analysis was performed.
 */
trait AnalysisTimeRepository {
  def get: Option[String]

  // hhmm format? make sure this works OK arround midnight; will need date also ?
  def put(time: String): Unit
}

class AnalysisTimeRepositoryImpl(filename: String) extends AnalysisTimeRepository {

  private val file = Path.fromString(filename)

  def get: Option[String] = {
    if (file.exists) {
      Some(file.string)
    }
    else {
      None
    }
  }

  def put(time: String): Unit = {
    file.write(time)
  }
}
