package kpn.core.engine.changes.ignore

import kpn.core.load.data.LoadedNode
import kpn.shared.Fact

trait IgnoredNodeAnalyzer {
  def analyze(node: LoadedNode): Seq[Fact]
  def displayAnalyze(node: LoadedNode, orphan: Boolean): Boolean
}
