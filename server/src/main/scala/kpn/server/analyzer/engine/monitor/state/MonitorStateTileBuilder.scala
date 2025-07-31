package kpn.server.analyzer.engine.monitor.state

import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile

trait MonitorStateTileBuilder {

  def build(state: MonitorState): Seq[MonitorStateTile]
}
