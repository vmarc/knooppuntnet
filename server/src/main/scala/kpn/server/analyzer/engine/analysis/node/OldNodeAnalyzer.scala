package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.Tags

trait OldNodeAnalyzer {

  def names(tags: Tags): Seq[NodeName]

}
