package kpn.server.repository

import kpn.server.analyzer.engine.changes.data.AnalysisData

trait AnalysisDataRepository {

  def save(analysisData: AnalysisData): Unit

  def get(): AnalysisData

}
