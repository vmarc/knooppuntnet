package kpn.server.analyzer.engine.analysis

import kpn.api.custom.Timestamp
import kpn.api.id.WithStringId

object AnalysisStatus {
  val id: String = "analysis"

  def apply(timestamp: Timestamp): AnalysisStatus = {
    AnalysisStatus(id, timestamp = timestamp)
  }
}

case class AnalysisStatus(
  _id: String,
  timestamp: Timestamp
) extends WithStringId
