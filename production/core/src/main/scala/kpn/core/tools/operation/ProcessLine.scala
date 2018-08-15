package kpn.core.tools.operation

case class ProcessLine(line: String) {
  def pid: String = line.substring(8, 14)
  def start: String = line.substring(24, 29)
  def elapsed: String = line.substring(39, 47)
}
