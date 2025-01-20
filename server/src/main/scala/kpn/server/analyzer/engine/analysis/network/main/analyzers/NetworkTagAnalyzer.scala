package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact

import scala.collection.mutable.ListBuffer

object NetworkTagAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkTagAnalyzer(context).analyze()
  }
}

class NetworkTagAnalyzer(context: NetworkAnalysisContext) {

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
