package kpn.core.repository

import kpn.core.engine.changes.data.AnalysisData

trait AnalysisDataRepository {

  def save(analysisData: AnalysisData): Unit

  def get(): AnalysisData

}
