package kpn.core.tools.operation

import scala.sys.process.Process

class ProcessReporterImpl extends ProcessReporter {
  def processes: List[String] = {
    Process("ps -ef").lineStream.toList
  }
}
