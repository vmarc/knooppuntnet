package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

@Component
class NetworkInfoChangeAnalyzer(database: Database) extends NetworkAnalyzer {

  private val log = Log(classOf[NetworkInfoChangeAnalyzer])

  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    val changeCount = queryNetworkChangeCount(context.network._id)
    context.copy(
      changeCount = changeCount
    )
  }

  private def queryNetworkChangeCount(networkId: Long): Long = {
    log.debugElapsed {
      val filter = equal("networkId", networkId)
      val count = database.networkInfoChanges.countDocuments(filter)
      (s"network $networkId change count: $count", count)
    }
  }
}
