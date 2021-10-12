package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import org.springframework.stereotype.Component

import scala.collection.mutable.ListBuffer

@Component
class NetworkInfoTagAnalyzer extends NetworkInfoAnalyzer {

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    val facts = ListBuffer[Fact]()
    // TODO MONGO other tag related analysis

    if (!context.networkDoc.tags.has("name")) {
      facts += Fact.NameMissing
    }

    context.copy(
      facts = context.facts ++ facts
    )
  }
}
