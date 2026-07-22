package kpn.core.common

case class TimerState(
  epochTimeElapsed: Boolean,
  cpuTimeElapsed: Boolean,
  epochElapsed: Long,
  cpuElapsed: Option[Long]
) {

  def isElapsed: Boolean = epochTimeElapsed && cpuTimeElapsed

}
