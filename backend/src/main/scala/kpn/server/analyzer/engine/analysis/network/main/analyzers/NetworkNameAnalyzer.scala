package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.data.Tagable

object NetworkNameAnalyzer extends NetworkAnalyzer {

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
    "Radknotenpunktsystem ",
    "Radeln nach Zahlen ",
    "Wanderwegenetz ",
    "Wandernetzwerk ",
    "Cycle node network ",
    "Ruiternetwerk ",
    "Ruiterroute ",
    "Ruiterroutenetwerk ",
    "Ruiter- en mennetwerk ",
    "Ruiter- en menroutenetwerk ",
    "Réseau Point-Nœud, ",
    "Réseau Point-Noeud, ",
    "Points-noeuds en ",
    "Motorbootnetwerk ",
    "Sloepennetwerk ",
    "Kanonetwerk ",
    "Skateroute-netwerk ",
    " (Walcheren)",
    "Réseau Pédestre - ",
    "Réseau pédestre du ",
    "Réseau pédestre des ",
    "Réseau pédestre d'",
    "Réseau pédestre de l'",
    "Réseau pédestre de la ",
    "Réseau pédestre de ",
    "Réseau pédestre ",
  )

  def name(tagable: Tagable): Option[String] = {
    val nameTagValue = tagable.tagValue("name")
    val prefixOption = NetworkNameAnalyzer.ignoredSubstrings.find(n => nameTagValue.contains(n))
    prefixOption match {
      case Some(substring) =>
        nameTagValue.map(_.replace(substring, "").trim)
      case None => nameTagValue.map(_.trim)
    }
  }

  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkNameAnalyzer(context).analyze
  }
}

class NetworkNameAnalyzer(context: NetworkAnalysisContext) {
  def analyze: NetworkAnalysisContext = {
    val nameTagValue = context.network.tagValue("name")
    val prefixOption = NetworkNameAnalyzer.ignoredSubstrings.find(n => nameTagValue.contains(n))
    val name = prefixOption match {
      case Some(substring) => nameTagValue.map(_.replace(substring, "").trim)
      case None => nameTagValue.map(_.trim)
    }
    context.copy(
      _name = Some(name)
    )
  }
}
