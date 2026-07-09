package kpn.core.tools.operation

case class ProcessLine(line: String) {

  def pid: String = line.substring(10, 16)

  def start: String = line.substring(28, 34)

  def elapsed: String = line.substring(43, 55).split(" ").head
}
