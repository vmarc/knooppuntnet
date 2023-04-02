package kpn.server.api.analysis.pages

import kpn.api.common.AnalysisStrategy
import kpn.api.common.ChangesPage
import kpn.api.common.Language
import kpn.api.common.changes.filter.ChangesParameters

trait ChangesPageBuilder {
  def build(language: Language, strategy: AnalysisStrategy, parameters: ChangesParameters): ChangesPage
}
