package kpn.server.analyzer.engine.analysis.node

object OldNodeNameAnalyzer {
  def normalize(nodeName: String): String = {
    if (nodeName.length == 1 && nodeName(0).isDigit) "0" + nodeName else nodeName
  }
}
