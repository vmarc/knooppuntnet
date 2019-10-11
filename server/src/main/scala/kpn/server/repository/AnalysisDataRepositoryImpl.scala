package kpn.server.repository

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.analysisDataFormat
import kpn.core.engine.changes.data.AnalysisData
import org.springframework.stereotype.Component

@Component
class AnalysisDataRepositoryImpl(mainDatabase: Database) extends AnalysisDataRepository {

  override def save(analysisData: AnalysisData): Unit = {
    mainDatabase.save("analysis-data", analysisDataFormat.write(analysisData))
  }

  override def get(): AnalysisData = {
    mainDatabase.optionGet("analysis-data", Couch.defaultTimeout).map(analysisDataFormat.read).get
  }
}
