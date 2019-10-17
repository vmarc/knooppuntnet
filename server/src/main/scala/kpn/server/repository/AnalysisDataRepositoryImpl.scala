package kpn.server.repository

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.analysisDataFormat
import kpn.server.analyzer.engine.changes.data.AnalysisData
import org.springframework.stereotype.Component

@Component
class AnalysisDataRepositoryImpl(analysisDatabase: Database) extends AnalysisDataRepository {

  override def save(analysisData: AnalysisData): Unit = {
    analysisDatabase.save("analysis-data", analysisDataFormat.write(analysisData))
  }

  override def get(): AnalysisData = {
    analysisDatabase.optionGet("analysis-data", Couch.defaultTimeout).map(analysisDataFormat.read).get
  }
}
