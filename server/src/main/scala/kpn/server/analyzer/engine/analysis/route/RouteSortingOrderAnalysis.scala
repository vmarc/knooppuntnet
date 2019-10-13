package kpn.server.analyzer.engine.analysis.route

case class RouteSortingOrderAnalysis(forwardOk: Boolean, backwardOk: Boolean, startTentaclesOk: Boolean, endTentaclesOk: Boolean) {
  def ok: Boolean = forwardOk && backwardOk && startTentaclesOk && endTentaclesOk
}
