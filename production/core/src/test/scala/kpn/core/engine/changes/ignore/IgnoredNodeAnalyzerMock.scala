package kpn.core.engine.changes.ignore

import kpn.core.load.data.LoadedNode
import kpn.shared.Fact

import scala.collection.mutable.ListBuffer

class IgnoredNodeAnalyzerMock extends IgnoredNodeAnalyzer {

  val ignoredNodeIds: ListBuffer[Long] = ListBuffer()

  override def analyze(node: LoadedNode): Seq[Fact] = {
    if (ignoredNodeIds.contains(node.id)) {
      Seq(Fact.IgnoreForeignCountry)
    }
    else {
      Seq()
    }
  }

  override def displayAnalyze(node: LoadedNode, orphan: Boolean): Boolean = true

}
