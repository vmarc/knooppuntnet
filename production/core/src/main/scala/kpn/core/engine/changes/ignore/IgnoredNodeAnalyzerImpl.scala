package kpn.core.engine.changes.ignore

import kpn.core.load.data.LoadedNode
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkType

class IgnoredNodeAnalyzerImpl() extends IgnoredNodeAnalyzer {
  override def analyze(loadedNode: LoadedNode): Seq[Fact] = {
    loadedNode.country match {
      case None => Seq(Fact.IgnoreForeignCountry)
      case Some(Country.de) =>
        if (loadedNode.networkTypes == Seq(NetworkType.hiking)) {
          Seq(Fact.IgnoreForeignCountry)
        }
        else {
          Seq()
        }

      case _ => Seq()
    }
  }

  override def displayAnalyze(node: LoadedNode, orphan: Boolean): Boolean = {
    !(orphan && node.node.tags.has("rcn_ref", "rvnsh"))
  }

}
