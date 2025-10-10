package kpn.server.analyzer.full

import kpn.api.common.ReplicationId
import kpn.core.common.TimestampUtil
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import org.springframework.stereotype.Component

@Component
class InitialFullAnalyzer(
  osmChangeRepository: OsmChangeRepository,
  mainFullAnalyzer: MainFullAnalyzer
) {
  def analyze(replicationId: ReplicationId): Unit = {
    val beginOsmChange = osmChangeRepository.get(replicationId)
    val timestampAfter = if (beginOsmChange.actions.isEmpty) {
      osmChangeRepository.timestamp(replicationId)
    }
    else {
      TimestampUtil.relativeSeconds(beginOsmChange.timestampUntil.get, 1)
    }
    mainFullAnalyzer.analyze(timestampAfter, None)
  }
}
