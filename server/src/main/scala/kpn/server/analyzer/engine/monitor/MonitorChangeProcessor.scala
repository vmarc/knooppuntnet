package kpn.server.analyzer.engine.monitor

import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait MonitorChangeProcessor {

  def load(timestamp: Timestamp): Unit

  def process(context: ChangeSetContext): Unit

}
