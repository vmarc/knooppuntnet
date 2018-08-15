package kpn.core.engine.analysis.route

case class RouteSortingOrderAnalysis(forwardOk: Boolean, backwardOk: Boolean, tentacleOk: Boolean) {
  def ok: Boolean = forwardOk && backwardOk && tentacleOk
}
