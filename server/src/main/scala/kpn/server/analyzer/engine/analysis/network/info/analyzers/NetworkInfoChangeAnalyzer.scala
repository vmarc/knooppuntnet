package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

@Component
class NetworkInfoChangeAnalyzer(database: Database) extends NetworkInfoAnalyzer {

  private val log = Log(classOf[NetworkInfoChangeAnalyzer])

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    val changeCount = queryNetworkChangeCount(context.networkDoc._id)
    context.copy(
      changeCount = changeCount
    )
  }

  private def queryNetworkChangeCount(networkId: Long): Long = {
    log.debugElapsed {
      val filter = equal("networkId", networkId)
      val count = database.networkChanges.countDocuments(filter)
      (s"network $networkId change count: $count", count)
    }
  }
}
