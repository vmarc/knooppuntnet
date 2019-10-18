package kpn.server.repository

import kpn.core.db.couch.Couch
import kpn.core.db.couch.OldDatabase
import kpn.core.db.json.JsonFormats.analysisDataFormat
import kpn.server.analyzer.engine.changes.data.AnalysisData
import org.springframework.stereotype.Component

@Component
class AnalysisDataRepositoryImpl(oldAnalysisDatabase: OldDatabase) extends AnalysisDataRepository {

  override def save(analysisData: AnalysisData): Unit = {
    oldAnalysisDatabase.save("analysis-data", analysisDataFormat.write(analysisData))
  }

  override def get(): AnalysisData = {
    oldAnalysisDatabase.optionGet("analysis-data", Couch.defaultTimeout).map(analysisDataFormat.read).get
  }
}
