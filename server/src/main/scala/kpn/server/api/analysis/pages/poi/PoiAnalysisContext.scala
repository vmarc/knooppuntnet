package kpn.server.api.analysis.pages.poi

import kpn.api.common.Poi
import kpn.api.common.PoiAnalysis
import kpn.api.custom.Tag

case class PoiAnalysisContext(
  poi: Poi,
  processedTagKeys: Seq[String],
  ignoredTagKeys: Seq[String],
  ignoredTagKeyValues: Seq[Tag],
  analysis: PoiAnalysis) {
}
