package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact

import scala.collection.mutable.ListBuffer

object NetworkInfoTagAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkInfoTagAnalyzer(context).analyze()
  }
}

class NetworkInfoTagAnalyzer(context: NetworkAnalysisContext) {

  def analyze(): NetworkAnalysisContext = {

    val facts = ListBuffer[Fact]()

    if (!context.network.hasTag("name")) {
      facts += Fact.NameMissing
    }

    context.copy(
      facts = context.facts ++ facts
    )
  }
}
