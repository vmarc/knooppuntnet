package kpn.core.engine.analysis

import kpn.shared.data.Relation

object NetworkNameAnalyzer {
  val ignoredSubstrings = Seq(
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
    "Cycle node network ",
    "Ruiternetwerk ",
    "Ruiterroute ",
    "Ruiterroutenetwerk ",
    "Motorbootnetwerk ",
    "Sloepennetwerk ",
    "Kanonetwerk ",
    " (Walcheren)"
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
