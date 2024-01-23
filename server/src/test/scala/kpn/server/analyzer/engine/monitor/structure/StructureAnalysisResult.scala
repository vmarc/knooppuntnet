package kpn.server.analyzer.engine.monitor.structure

case class StructureAnalysisResult(
  reference: Seq[String],
  analysis: Seq[Seq[String]]
)
