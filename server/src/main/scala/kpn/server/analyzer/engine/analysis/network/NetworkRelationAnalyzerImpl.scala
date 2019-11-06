package kpn.server.analyzer.engine.analysis.network

import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.shared.Timestamp
import kpn.shared.data.Relation
import org.springframework.stereotype.Component

@Component
class NetworkRelationAnalyzerImpl(
  relationAnalyzer: RelationAnalyzer,
  countryAnalyzer: CountryAnalyzer
) extends NetworkRelationAnalyzer {

  def analyze(relation: Relation): NetworkRelationAnalysis = {

    val nodes = relationAnalyzer.referencedNetworkNodes(relation).toSeq.sortBy(_.id)
    val routeRelations = relationAnalyzer.referencedRoutes(relation).toSeq.sortBy(_.id)
    val networkRelations = relationAnalyzer.referencedNetworks(relation).toSeq.sortBy(_.id)

    val nodeRefs = nodes.map(_.id)
    val routeRefs = routeRelations.map(_.id)
    val networkRefs = networkRelations.map(_.id)

    val country = if (nodes.isEmpty) {
      countryAnalyzer.country(nodes)
    }
    else {
      countryAnalyzer.relationCountry(relation)
    }

    val lastUpdate: Timestamp = {
      val relationUpdates = Seq(relation.timestamp)
      val nodeUpdates = nodes.map(_.timestamp)
      val routeUpdates = routeRelations.map(_.timestamp)
      val networkUpdates = networkRelations.map(_.timestamp)
      val timestamps = relationUpdates ++ nodeUpdates ++ routeUpdates ++ networkUpdates
      timestamps.max
    }

    val elementIds = relationAnalyzer.toElementIds(relation)

    NetworkRelationAnalysis(
      country,
      nodes,
      routeRelations,
      networkRelations,
      nodeRefs,
      routeRefs,
      networkRefs,
      lastUpdate,
      elementIds
    )
  }
}
