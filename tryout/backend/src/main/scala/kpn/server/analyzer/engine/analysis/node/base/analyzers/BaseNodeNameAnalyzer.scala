package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.server.analyzer.engine.analysis.node.NodeNameAnalyzer

case class Name(name: String, proposed: Boolean)

object BaseNodeNameAnalyzer extends BaseNodeAnalyzer {
  def analyze(context: BaseNodeAnalysisContext): BaseNodeAnalysisContext = {
    val names = NodeNameAnalyzer.analyze(context.node)
    val name = if (names.nonEmpty) Some(names.map(_.name).distinct.sorted.mkString(" / ")) else None
    val active = names.nonEmpty
    context.copy(
      _name = Some(name),
      _names = Some(names),
      active = active
    )
  }
}
