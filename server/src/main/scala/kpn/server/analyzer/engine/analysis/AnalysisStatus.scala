package kpn.server.analyzer.engine.analysis

import kpn.api.base.WithStringId
import kpn.api.custom.Timestamp

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
