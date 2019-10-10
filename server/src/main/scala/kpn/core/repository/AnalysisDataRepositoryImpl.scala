package kpn.core.repository

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.analysisDataFormat
import kpn.core.engine.changes.data.AnalysisData

class AnalysisDataRepositoryImpl(database: Database) extends AnalysisDataRepository {

  override def save(analysisData: AnalysisData): Unit = {
    database.save("analysis-data", analysisDataFormat.write(analysisData))
  }

  override def get(): AnalysisData = {
    database.optionGet("analysis-data", Couch.defaultTimeout).map(analysisDataFormat.read).get
  }
}
