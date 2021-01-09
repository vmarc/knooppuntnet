package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogAnalysisContext
import kpn.core.tools.log.LogRecord

object AnalysisAnalyzer extends LogRecordAnalyzer {

  private val patterns = Seq(
    """/(nl|en|fr|de)/node/\d+""".r, // old urls
    """/(nl|en|fr|de)/route/\d+""".r, // old urls
    """/(nl|en|fr|de)/network/\d+""".r, // old urls
    """/(nl|en|fr|de)/network-map/\d+""".r, // old urls
    """/(nl|en|fr|de)/network-nodes/\d+""".r, // old urls
    """/(nl|en|fr|de)/network-routes/\d+""".r, // old urls
    """/(nl|en|fr|de)/glossary""".r, // old urls

    """/(nl|en|fr|de)/analysis""".r,
    """/(nl|en|fr|de)/analysis/overview""".r,
    """/(nl|en|fr|de)/analysis/changes""".r,
    """/(nl|en|fr|de)/analysis/node/\d+""".r,
    """/(nl|en|fr|de)/analysis/node/\d+/map""".r,
    """/(nl|en|fr|de)/analysis/node/\d+/changes""".r,
    """/(nl|en|fr|de)/analysis/route/\d+""".r,
    """/(nl|en|fr|de)/analysis/route/\d+/map""".r,
    """/(nl|en|fr|de)/analysis/route/\d+/changes""".r,
    """/(nl|en|fr|de)/analysis/network/\d+""".r,
    """/(nl|en|fr|de)/analysis/network/\d+/map""".r,
    """/(nl|en|fr|de)/analysis/network/\d+/nodes""".r,
    """/(nl|en|fr|de)/analysis/network/\d+/routes""".r,
    """/(nl|en|fr|de)/analysis/network/\d+/changes""".r,
    """/(nl|en|fr|de)/analysis/(cycling|hiking|horseriding|motorboat|canoe|inlineskating)/.*""".r,
    """/(nl|en|fr|de)/analysis/changeset/\d+/\d+""".r,
    """/(nl|en|fr|de)/""".r,
    """/(nl|en|fr|de)/settings""".r,
    """/(nl|en|fr|de)/about""".r,
    """/robots.txt""".r,

    """/(nl|en|fr|de)/map""".r, // planner
    """/(nl|en|fr|de)/map/(cycling|hiking|horse-riding|motorboat|canoe|inline-skating)""".r, // planner
  )

  def analyze(record: LogRecord, context: LogAnalysisContext): LogAnalysisContext = {
    if (patterns.exists(pattern => pattern.matches(record.path))) {
      val key = if (context.recordAnalysis.robot) "analysis-robot" else "analysis"
      context.withValue(key).copy(recordAnalysis = context.recordAnalysis.copy(analysis = true))
    }
    else {
      context
    }
  }

}
