package kpn.api.common

sealed trait AnalysisMode

case object LOCATION extends AnalysisMode

case object NETWORK extends AnalysisMode

object AnalysisMode {
  val all: Seq[AnalysisMode] = Seq(LOCATION, NETWORK)
}
