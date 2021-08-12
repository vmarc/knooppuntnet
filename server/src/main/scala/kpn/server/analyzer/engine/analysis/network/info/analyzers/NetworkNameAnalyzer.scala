package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object NetworkNameAnalyzer extends NetworkInfoAnalyzer {

  private val ignoredSubstrings = Seq(
    "(Wandelnetwerk Goes-Kapelle)",
    "Fietsnetwerk ",
    "Fietsroutenetwerk B ",
    "Fietsroutenetwerk ",
    "Wandelnetwerk Antwerpse Kempen - ",
    "Wandelroutenetwerk NL ",
    "Wandelroutenetwerk B ",
    "Wandelroutenetwerk",
    "Wandelnetwerk ",
    "Wandelrnetwerk ",
    "Knotenpunktnetz ",
    "Knotenpunktnetzwerk ",
    "Knotenpunktsystem ",
    "Knotenpunktwegweisung ",
    "Wanderwegenetz ",
    "Cycle node network ",
    "Ruiternetwerk ",
    "Ruiterroute ",
    "Ruiterroutenetwerk ",
    "Ruiter- en mennetwerk ",
    "Ruiter- en menroutenetwerk ",
    "Réseau Point-Nœud, ",
    "Points-noeuds en ",
    "Motorbootnetwerk ",
    "Sloepennetwerk ",
    "Kanonetwerk ",
    "Skateroute-netwerk ",
    " (Walcheren)",
    "Réseau Pédestre - ",
    "Réseau pédestre "
  )

  def name(tags: Tags): String = {
    val nameTagValue = tags("name").getOrElse("no-name")
    val prefixOption = NetworkNameAnalyzer.ignoredSubstrings.find(n => nameTagValue.contains(n))
    prefixOption match {
      case Some(substring) => nameTagValue.replace(substring, "").trim
      case None => nameTagValue.trim
    }
  }

  def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkNameAnalyzer(context).analyze
  }
}

class NetworkNameAnalyzer(context: NetworkInfoAnalysisContext) {
  def analyze: NetworkInfoAnalysisContext = {
    val nameTagValue = context.networkDoc.tags("name").getOrElse("no-name")
    val prefixOption = NetworkNameAnalyzer.ignoredSubstrings.find(n => nameTagValue.contains(n))
    val name = prefixOption match {
      case Some(substring) => nameTagValue.replace(substring, "").trim
      case None => nameTagValue.trim
    }
    context.copy(
      name = name
    )
  }
}
