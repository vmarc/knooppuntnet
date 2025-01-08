package kpn.server.analyzer.engine.analysis.network.base.analyzers

import kpn.api.common.data.Tagable

object BaseNetworkNameAnalyzer extends BaseNetworkAnalyzer {

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

  override def analyze(context: BaseNetworkAnalysisContext): BaseNetworkAnalysisContext = {
    val name = determineName(context.relation)
    context.copy(
      _name = Some(name)
    )
  }

  private def determineName(tagable: Tagable): Option[String] = {
    tagable.tagValue("name").map { nameTagValue =>
      val prefixOption = ignoredSubstrings.find(n => nameTagValue.contains(n))
      prefixOption match {
        case Some(substring) => nameTagValue.replace(substring, "").trim
        case None => nameTagValue.trim
      }
    }
  }
}
