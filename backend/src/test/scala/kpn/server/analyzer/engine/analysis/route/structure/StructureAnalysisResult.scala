package kpn.server.analyzer.engine.analysis.route.structure

case class StructureAnalysisResult(
  reference: Seq[String],
  analysis: Seq[Seq[String]]
)
