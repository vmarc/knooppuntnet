package kpn.server.analyzer.engine.analysis

import kpn.shared.data.Relation

object NetworkNameAnalyzer {
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
    "Réseau Pédestre - "
  )
}

class NetworkNameAnalyzer(networkRelation: Relation) {

  def name: String = {
    val name = networkRelation.tags("name").getOrElse("no-name")
    val prefixOption = NetworkNameAnalyzer.ignoredSubstrings.find(n => name.contains(n))
    prefixOption match {
      case Some(substring) => name.replace(substring, "").trim
      case None => name.trim
    }
  }
}
