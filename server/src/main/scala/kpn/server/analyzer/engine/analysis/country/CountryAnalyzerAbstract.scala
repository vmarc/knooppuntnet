package kpn.server.analyzer.engine.analysis.country

import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.shared.Country
import kpn.shared.LatLon
import kpn.shared.data.Relation

abstract class CountryAnalyzerAbstract(relationAnalyzer: RelationAnalyzer) extends CountryAnalyzer {

  override def relationCountry(relation: Relation): Option[Country] = {
    val nodes = relationAnalyzer.referencedNetworkNodes(relation)
    if (nodes.nonEmpty) {
      country(nodes)
    }
    else {
      val ways = relationAnalyzer.referencedWays(relation)
      val nodes = ways.flatMap { w =>
        if (w.nodes.nonEmpty) {
          Seq(w.nodes.head, w.nodes.last)
        }
        else {
          Seq.empty
        }
      }
      country(nodes)
    }
  }

  protected def doCountry(latLons: Iterable[LatLon]): Option[Country] = {
    val c = latLons.toSeq.flatMap { latLon =>
      countries(latLon)
    }
    if (c.nonEmpty) {
      val countryCounts: Map[Country, Int] = c.groupBy(identity).map(e => e._1 -> e._2.size)
      val maxCountryCount = countryCounts.values.max
      val countriesWithMaxCount = countryCounts.filter(_._2 == maxCountryCount).keys
      Some(countriesWithMaxCount.minBy(_.domain))
    }
    else {
      None
    }
  }
}
