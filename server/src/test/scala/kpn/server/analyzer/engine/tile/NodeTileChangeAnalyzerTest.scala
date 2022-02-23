package kpn.server.analyzer.engine.tile

import kpn.core.TestObjects
import kpn.core.doc.NodeDoc
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import org.scalamock.scalatest.MockFactory

class NodeTileChangeAnalyzerTest extends UnitTest with MockFactory with TestObjects {

  test("no tiles when no change with impact") {
    val before = buildNodeDoc()
    val after = before.copy()
    impactedTiles(before, after) should equal(Seq.empty)
  }

  private def buildNodeDoc(): NodeDoc = {
    newNodeDoc(
      1001L
    )
  }

  private def impactedTiles(before: NodeDoc, after: NodeDoc): Seq[String] = {
    val tileChangeAnalyzer = new NodeTileChangeAnalyzerImpl(new TileDataNodeBuilderImpl())
    tileChangeAnalyzer.impactedTiles(before, after)
  }
}
