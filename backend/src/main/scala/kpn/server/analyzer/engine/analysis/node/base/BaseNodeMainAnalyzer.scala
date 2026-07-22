package kpn.server.analyzer.engine.analysis.node.base

import kpn.api.common.data.raw.Raw
import kpn.api.common.data.raw.RawNode
import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.NodeBaseData
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeAnalysisContext
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeCountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeNameAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeSurveyAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeTileAnalyzer
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
@Profile(Array("analysis"))
class BaseNodeMainAnalyzer(
  countryAnalyzer: BaseNodeCountryAnalyzer,
  locationAnalyzer: BaseNodeLocationAnalyzer,
  tileAnalyzer: BaseNodeTileAnalyzer
) {

  def analyze(node: RawNode): Option[BaseNodeDoc] = {
    Log.context(f"node=${node.id}%07d") {
      val context = BaseNodeAnalysisContext(node)
      val analyzers: List[BaseNodeAnalyzer] = List(
        BaseNodeNameAnalyzer,
        BaseNodeSurveyAnalyzer,
        countryAnalyzer,
        locationAnalyzer,
        tileAnalyzer,
      )
      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[BaseNodeAnalyzer], context: BaseNodeAnalysisContext): Option[BaseNodeDoc] = {
    if (analyzers.isEmpty) {
      Some(
        BaseNodeDoc(
          _id = context.node.id,
          active = context.active,
          base = NodeBaseData(
            raw = Raw(
              version = context.node.version,
              changeSetId = context.node.changeSetId,
              timestamp = context.node.timestamp,
              tags = context.node.tags
            ),
            name = context.name,
            names = context.names,
            lastSurvey = context.lastSurvey,
            latitude = context.node.latitude,
            longitude = context.node.longitude,
            country = context.country,
            locations = context.locations
          ),
          facts = context.facts,
          tiles = context.tiles,
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
