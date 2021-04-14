package kpn.server.analyzer.engine.analysis.route.analyzers

object NodeNameAnalyzer {

  def normalize(nodeName: String): String = {
    if (nodeName.length == 1 && nodeName(0).isDigit) "0" + nodeName else nodeName
  }

}
