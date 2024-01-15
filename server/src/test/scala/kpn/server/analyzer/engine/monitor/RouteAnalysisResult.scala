package kpn.server.analyzer.engine.monitor

case class RouteAnalysisResult(
  reference: Seq[String],
  analysis: Seq[Seq[String]]
)
