package kpn.core.engine.changes.ignore

import kpn.core.engine.analysis.NetworkRelationAnalysis
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.load.data.LoadedNetwork
import kpn.core.util.Log
import kpn.shared.Fact

class IgnoredNetworkAnalyzerImpl(
  countryAnalyzer: CountryAnalyzer
) extends IgnoredNetworkAnalyzer {

  private val log = Log(classOf[IgnoredNetworkAnalyzer])

  override def analyze(networkRelationAnalysis: NetworkRelationAnalysis, network: LoadedNetwork): Seq[Fact] = {
    if (hasSufficientNodesToDetermineCountry(networkRelationAnalysis)) {
      foreignCountry(network)
    }
    else {
      Seq()
    }
  }

  private def hasSufficientNodesToDetermineCountry(networkRelationAnalysis: NetworkRelationAnalysis): Boolean = {
    if (networkRelationAnalysis.nodes.size > 5) {
      true
    }
    else {
      networkNodeCount(networkRelationAnalysis) > 5
    }
  }

  private def networkNodeCount(networkRelationAnalysis: NetworkRelationAnalysis): Int = {
    networkRelationAnalysis.routeRelations.map { routeRelation =>
      val nodeCount = routeRelation.nodeMembers.size
      val wayNodeCount = routeRelation.wayMembers.map(_.way.nodes.size).sum
      nodeCount + wayNodeCount
    }.sum
  }

  private def foreignCountry(network: LoadedNetwork): Seq[Fact] = {
    val country = countryAnalyzer.relationCountry(network.relation)
    if (country.isEmpty) {
      log.info( s"""Ignore network ${network.networkId} "${network.name}": foreign country""")
      Seq(Fact.IgnoreForeignCountry)
    }
    else {
      Seq()
    }
  }

}
