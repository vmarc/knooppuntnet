package kpn.api.common

sealed trait AnalysisStrategy

case object LOCATION extends AnalysisStrategy

case object NETWORK extends AnalysisStrategy

object AnalysisStrategy {
  val all: Seq[AnalysisStrategy] = Seq(LOCATION, NETWORK)
}
