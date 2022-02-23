package kpn.server.analyzer.engine.tile

import kpn.core.doc.NodeDoc

trait NodeTileChangeAnalyzer {

  def impactedTiles(before: NodeDoc, after: NodeDoc): Seq[String]
}
