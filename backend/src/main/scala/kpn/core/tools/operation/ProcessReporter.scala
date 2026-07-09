package kpn.core.tools.operation

import scala.sys.process.Process

class ProcessReporter {
  def processes: List[String] = {
    Process("ps -ef").lazyLines.toList
  }
}
