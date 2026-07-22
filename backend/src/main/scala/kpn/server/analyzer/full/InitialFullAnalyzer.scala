package kpn.server.analyzer.full

import kpn.api.common.ReplicationId
import kpn.api.time.TimestampUtil
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class InitialFullAnalyzer(
  osmChangeRepository: OsmChangeRepository,
  mainFullAnalyzer: MainFullAnalyzer,
  analyzerInitializerEnabled: Boolean
) {
  def analyze(replicationId: ReplicationId): Unit = {
    if (analyzerInitializerEnabled) {
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
}
